echo off
cls
if "%1" == "" goto usage
if not exist %1\nul goto noLocate
echo If the error "Unable to create directory" appears
echo onscreen, Please disregard message. Thank-you.
echo ÿ
md %1\sierra
md %1\sierra\QG2demo
echo ÿ
echo Copying and Extracting files, please wait ...
copy demo.exe %1\sierra\QG2demo
copy QG2demo.bat %1\sierra
copy exists.com %1\sierra
%1
cd \sierra\QG2demo
demo
cd..
cls
echo ÿ
echo Finished Copying and extracting.
echo ÿ
echo To play the Quest for Glory II demo,
echo     type: QG2demo
echo ÿ
echo If you have not run the install program.
echo Typing "QG2demo" will automatically run the install.
echo then play the demo.
echo ÿ
echo NOTE: The last step of the install, when it states:
echo ÿ
echo           ศอออออ(ESC or C through Z)อออออผ
echo ÿ
echo       press the ESC key.
goto end
:noLocate
echo ERROR: Illegal Drive %1
:usage
type read.me
:end

