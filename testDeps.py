try:
    import PyWigit
except Exception as e:
    fail = True
    print("Could not find pygame, automaticaly installing dependancy")
    import pip
    pip.main(["install", "pygame", "--break-system-packages"])

try:
    import noise
except Exception as e:
    print("Could not find noise, automaticaly installing dependancy")
    import pip
    pip.main(["install"], "noise", "--break-system-packages")
