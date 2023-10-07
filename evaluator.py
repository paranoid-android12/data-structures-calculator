import sys

try:
    #Initializes mainNum
    mainNum = sys.argv[1]
    # mainNum = "5+5,5,20"
    variables = mainNum.split(',')  # Split the input string using the comma delimiter

    sum = 0
    func = variables[0]
    tempA = int(variables[1])
    tempB = int(variables[2])


    if len(variables) != 3:
        raise ValueError("Expected 3 variables")

    for i in range(tempA, tempB+1):
        sum += eval(func)

    print(sum)


except Exception as e:
    print(f"Err")
    sys.exit(1)