import sys

try:
    #Initializes mainNum
    mainNum = sys.argv[1]
    # mainNum = "i,1,10,2"
    variables = mainNum.split(',')  # Split the input string using the comma delimiter
    sum = 0
    mainSum = 0
    prod = 1
    mainProd = 1
    
    
    
    func = variables[0]
    outA = int(variables[1])
    outB = int(variables[2])
    tempC = int(variables[3])
    tempD = int(variables[4])
    type = int(variables[5])
    
    func = func.replace("^", "**")

    
    if len(variables) != 6:
        raise ValueError("Expected 4 variables")

    for i in range(outA, outB+1):
        for x in range(tempC, tempD+1):
            if(type == 3): sum += eval(func)
            elif(type == 4): prod *= eval(func)
        
        if(type == 3): mainSum += sum
        elif(type == 4): mainProd *= prod
        sum = 0
        prod = 1
        
    if(type == 3):print(mainSum)
    elif(type == 4):print(mainProd)


except Exception as e:
    print(f"Err")
    sys.exit(1)