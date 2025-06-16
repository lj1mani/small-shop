Set WshShell = CreateObject("WScript.Shell") 
WshShell.Run chr(34) & "D:\Code\SmallShop\out\artifacts\SmallShop_jar\SmallShop.jar" & Chr(34), 0
Set WshShell = Nothing