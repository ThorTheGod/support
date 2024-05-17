from tracemalloc import start
import numpy as np
import pandas as pd
import sys
import scipy
from scipy import optimize as op

pd.set_option('display.max_columns', None)  # 显示所有列
pd.set_option('display.max_rows', None)  # 显示所有行
WS = pd.read_excel(io=r"D:\IDEA\IdeaProject\support\src\main\resources\output\sim.xls", header=None)
WS_np = np.array(WS)
a = pd.DataFrame()
# 属性列
left = pd.DataFrame()
# 要素列
right = pd.DataFrame()
df = pd.DataFrame()

input_1 = -0.4
input_2 = -0.6
rowNum = WS.shape[0]
# 获取列数data.shape[1]
# 获取行数data.shape[0]
# 第0列第1行WS[0][0]
# 将4列属性和4列要素合并为4列总和
for i in range(4):
    a[i] = WS[i] + WS[i + 4]
    left[i] = WS[i]
    right[i] = WS[i + 4]
# 对每一个方案利用线性规划计算相似度权重
for item in range(rowNum):
    data = []
    #
    for i in range(len(WS_np)):
        if i == item:
            continue
        dea_line_left = list(WS_np[i][:4])
        dea_line_left.extend([input_1, 0, 0])
        dea_line_right = list(WS_np[i][4:])
        dea_line_right.extend([input_2, 0, 0])
        # print(dea_line_left, dea_line_right)
        data.append(dea_line_left)
        data.append(dea_line_right)

    c = np.array([0, 0, 0, 0, 0, -1, -1])
    A_ub = np.array(data)
    # B_ub = np.array(
    #     [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0])
    B_ub = np.zeros((rowNum - 1) * 2)
    y_line_left = list(WS_np[item][:4])
    y_line_left.extend([input_1, 1, 0])
    y_line_right = list(WS_np[item][4:])
    y_line_right.extend([input_2, 0, 1])
    A_eq = np.array(
        [y_line_left, y_line_right, [0, 0, 0, 0, 1, 0, 0]])
    B_eq = np.array([0, 0, 1])
    x1 = (0, 1)
    x2 = (0, 1)
    x3 = (0, 1)
    x4 = (0, 1)
    x5 = (0, 1)
    x6 = (0, 1)
    x7 = (0, 1)
    res = op.linprog(-c, A_ub, B_ub, A_eq, B_eq, bounds=(x1, x2, x3, x4, x5, x6, x7))
    # print("第{", str(item + 1), "}次结果:",res.x[:4])
    b = pd.DataFrame(res.x[:4])
    b = b.T
    df = df.append(b, ignore_index=True)

# 将两个dataframe左右拼接得到24X8矩阵,a现在是8列：4列和，4列权重
a = pd.concat([a, df], axis=1, ignore_index=True)
b = pd.DataFrame(np.zeros([rowNum, rowNum]))
element = pd.DataFrame(np.zeros([rowNum, rowNum]))

# 计算24X8矩阵，得到24X24矩阵，并计算同行均值得到24个案例的最终相似度
# 总相似度
sim = np.zeros(rowNum)
# 要素相似度
elementSim = np.zeros(rowNum)

# 外层循环：行数，即案例数
i = 0
total = 0
elementS = 0
for i in range(rowNum):
    # 中层循环：列数
    for j in range(rowNum):
        # 内层循环4次，每结束一次内层循环就获得一个相似度值
        for q in range(4):
            b[j][i] = a[q][i] * a[q + 4][j] + b[j][i]
            element[j][i] = right[q][i] * a[q + 4][j] + element[j][i]

        # 每计算出一个相似度值就加起来
        total = total + b[j][i]
        elementS = elementS + element[j][i]
    sim[i] = total / rowNum
    elementSim[i] = elementS / (rowNum * 0.6)
    # 新一行重新计算均值
    total = 0
    elementS = 0

elementSim = pd.DataFrame({'elementSim': elementSim})

sim = pd.DataFrame({'sim': sim})
# 接收输入的方案id数组并转化为np数组
d = sys.argv[1].replace(" ", "").strip("[").strip("]")
d = pd.DataFrame({'id': np.array(d.split(","))})
# d = pd.DataFrame({'id':np.array([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25])})
threshold = float(d.loc[rowNum, 'id'])
d = d.drop(rowNum)

# d[id,总相似度]
d = pd.concat([d, sim], axis=1)
# left[id,总相似度,要素相似度]
left = pd.concat([d, elementSim], axis=1)
d = d.sort_values(by='sim', axis=0, ascending=False)
left = left.sort_values(by='sim', axis=0, ascending=False)
d = d.values

j = 0
q = 0

while j < 3 and q < rowNum:
    if left.loc[q, 'elementSim'] > threshold:
        print(d[q])
        j = j + 1
    q = q + 1

# a.to_excel('sim3.xls',sheet_name="sheet1",index=False,header=False)
