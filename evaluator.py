import sys

try:
    #Initializes mainNum
    mainNum = sys.argv[1]
    # mainNum = "205*."
    mainNum.replace(" ", "")

    #Edge cases filter
    if mainNum[-1] in ('+', '-', '*', '/'): mainNum = mainNum[:-1] #If left out operations exist
    if mainNum[-1] == '.': mainNum = mainNum[:-1] + '0'            #If last value is a decimal
        
    
    #Evaluate the string
    result = eval(mainNum)

    print(result)
except Exception as e:
    print(f"Err")
    sys.exit(1)
