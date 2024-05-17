WS = pd.read_excel('sim2.xlsx', header=None)
WS_np = np.array(WS)
input_1 = -0.4
input_2 = -0.6

for item in range(24):
    data = []
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
    # print(data)
    c = np.array([0, 0, 0, 0, 0, -1, -1])
    A_ub = np.array(data)
    B_ub = np.array(
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0])
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
    print(f"第{item+1}次结果",res.x)