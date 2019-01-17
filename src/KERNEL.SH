//KERNEL.SH
//Definitions of the kernel interface for the Script interpreter.

(define  kernel   $ffff)


//These External Defines are based on the SCI1 kernels.  SCI0 may have different kernal reference numbers.
/*(extern
   //Resource handling.
   Load              kernel   00
   UnLoad            kernel   01
   ScriptID          kernel   02
   DisposeScript     kernel   03

   //Object management.
   Clone             kernel   04
   DisposeClone      kernel   05
   IsObject          kernel   06
   RespondsTo        kernel   07

   //Pictures.
   DrawPic           kernel   08
   Show              kernel   09
   PicNotValid       kernel   10

   //Animated objects & views.
   Animate           kernel   11
   SetNowSeen        kernel   12
   NumLoops          kernel   13 // view
   NumCels           kernel   14 // view/loop
   CelWide           kernel   15 // view/loop/cel
   CelHigh           kernel   16 // view/loop/cel
   DrawCel           kernel   17 // view/loop/cel/left/top/priority/palette
   AddToPic          kernel   18

   //Window/dialog/controls.
   NewWindow         kernel   19
   GetPort           kernel   20
   SetPort           kernel   21
// SetPortRect       kernel   21    //this is the same kernel call as SetPort
   DisposeWindow     kernel   22
   DrawControl       kernel   23
   HiliteControl     kernel   24
   EditControl       kernel   25

   //Screen text.
   TextSize          kernel   26
   Display           kernel   27

   //Events.
   GetEvent          kernel   28
   GlobalToLocal     kernel   29
   LocalToGlobal     kernel   30
   MapKeyToDir       kernel   31

   //Menu bar & status line.
   DrawMenuBar       kernel   32
   MenuSelect        kernel   33
   AddMenu           kernel   34
   DrawStatus        kernel   35

   //Parsing.
// Parse             kernel   36
//; Said              kernel   37
//; SetSynonyms       kernel   38

   //Mouse functions.
   HaveMouse         kernel   39
   SetCursor         kernel   40


   //Save/restore/restart.
   SaveGame          kernel   41
   RestoreGame       kernel   42
   RestartGame       kernel   43
   GameIsRestarting  kernel   44

   //Sounds.
/**********************************************
*  DoSound in SCI0         kernel   45
***********************************************/
   //(define InitSound		0)  //sci.sh = sndINIT
   //(define PlaySound		1)  //sci.sh = sndPLAY
   //(define NextSound		2)  //sci.sh = sndNOP
   //(define KillSound		3)  //sci.sh = sndDISPOSE
   //(define SoundOn		4)  //sci.sh = sndSET_SOUND //NOTE: This definition changed between SCI0 and SCI1.1
   //(define StopSound		5)  //sci.sh = sndSTOP
   //(define PauseSound		6)  //sci.sh = sndPAUSE
   //(define RestoreSound	7)  //sci.sh = sndRESUME
   (define ChangeVolume		8)  //sci.sh = sndVOLUME
   //(define ChangeSndState	9)  //sci.sh = sndUPDATE
   //(define FadeSound		10) //sci.sh = sndFADE
   //(define NumVoices		11) //sci.sh = sndCHECK_DRIVER
   //(define (unknown)		12) //sci.sh = sndSTOP_ALL


/**********************************************
*  DoSound in SCI1.1       kernel   45
***********************************************/
   (define MasterVol    0)  //sci.sh = sndMASTER_VOLUME           SCI0 = sndINIT
   (define SoundOn      1)  //sci.sh = sndSET_SOUND               SCI0 = sndPLAY
   (define RestoreSound 2)  //sci.sh = sndRESTORE                 SCI0 = sndNOP
   (define NumVoices    3)  //sci.sh = sndGET_POLYPHONY           SCI0 = sndDISPOSE
   (define NumDACs      4)  //sci.sh = sndGET_AUDIO_CAPABILITY    SCI0 = sndSET_SOUND
   (define Suspend      5)  //sci.sh = sndSUSPEND                 SCI0 = sndSTOP
   (define InitSound    6)  //sci.sh = sndINIT                    SCI0 = sndPAUSE
   (define KillSound    7)  //sci.sh = sndDISPOSE                 SCI0 = sndRESUME
   (define PlaySound    8)  //sci.sh = sndPLAY                    SCI0 = sndVOLUME
   (define StopSound    9)  //sci.sh = sndSTOP                    SCI0 = sndUPDATE
   (define PauseSound   10) //sci.sh = sndPAUSE                   SCI0 = sndFADE
   (define FadeSound    11) //sci.sh = sndFADE                    SCI0 = sndCHECK_DRIVER
   (define HoldSound    12) //sci.sh = sndSET_HOLD                SCI0 = sndSTOP_ALL
   (define MuteSound    13) //sci.sh = sndDUMMY
   (define SetVol       14) //sci.sh = sndSET_VOLUME
   (define SetPri       15) //sci.sh = sndSET_PRIORITY
   (define SetLoop      16) //sci.sh = sndSET_LOOP
   (define UpdateCues   17) //sci.sh = sndUPDATE_CUES
   (define MidiSend     18) //sci.sh = sndSEND_MIDI
   (define SetReverb    19) //sci.sh = sndGLOBAL_REVERB

   // This will be removed after KQ5 cd and
   // Jones cd are shipped (DO NOT USE)
   (define ChangeSndState 20) //sci.sh = sndUPDATE

/*
   //List handling.
   NewList           kernel   46
   DisposeList       kernel   47
   NewNode           kernel   48
   FirstNode         kernel   49
   LastNode          kernel   50
   EmptyList         kernel   51
   NextNode          kernel   52
   PrevNode          kernel   53
   NodeValue         kernel   54
   AddAfter          kernel   55
   AddToFront        kernel   56
   AddToEnd          kernel   57
   FindKey           kernel   58
   DeleteKey         kernel   59

   //Mathematical functions.
   Random            kernel   60
   Abs               kernel   61
   Sqrt              kernel   62
   GetAngle          kernel   63
   GetDistance       kernel   64

   //Miscellaneous.
   Wait              kernel   65

/**********************************************************
* GetTime            kernel      66
* pass NO argument for 60ths second "ticks" value
***********************************************************/
   // 0 is undefined in SysTime  //sci.sh = gtTIME_ELAPSED = 0
   (define SYSTIME1  1) // returns HHHH|MMMM|MMSS|SSSS (1 sec resoulution, 12 Hr)  //sci.sh = gtTIME_OF_DAY = 1
   (define SYSTIME2  2) // returns HHHH|HMMM|MMMS|SSSS (2 sec resoulution 24 Hr)
   (define SYSDATE   3) // returns YYYY|YYYM|MMMD|DDDD (date since 1980)

/*
   //String handling.
   StrEnd            kernel   67
   StrCat            kernel   68
   StrCmp            kernel   69
   StrLen            kernel   70
   StrCpy            kernel   71
   Format            kernel   72
   GetFarText        kernel   73
   ReadNumber        kernel   74

   //Actor motion support.
   BaseSetter        kernel   75
   DirLoop           kernel   76
   CantBeHere        kernel   77
   OnControl         kernel   78
   InitBresen        kernel   79
   DoBresen          kernel   80
   Platform          kernel   81
   SetJump           kernel   82

   //Debugging support.
   SetDebug          kernel   83
   InspectObj        kernel   84
   ShowSends         kernel   85
// ShowObjs          kernel   86
   ShowFree          kernel   87

/***********************************************
* MemoryInfo         kernel      88
************************************************/
   (define LargestPtr    0) //sci.sh = miFREEHEAP
   (define FreeHeap      1) //sci.sh = miLARGESTPTR
   (define LargestHandle 2) //sci.sh = miLARGESTHUNK
   (define FreeHunk      3) //sci.sh = miFREEHUNK
   (define TotalHunk     4) //sci.sh = <undefined>

/***********************************************
* StackUsage         kernel      89
************************************************/
   (define PStackSize 0)
   (define PStackMax  1)
   (define PStackCur  2)
   (define MStackSize 3)
   (define MStackMax  4)
   (define MStackCur  5)

/***********************************************
* Profiler           kernel      90
************************************************/
   (define PRO_OPEN   0)
   (define PRO_CLOSE  1)
   (define PRO_ON     2)
   (define PRO_OFF    3)
   (define PRO_CLEAR  4)
   (define PRO_REPORT 5)
   (define TRACE_ON   6)
   (define TRACE_OFF  7)
   (define TRACE_RPT  8)

/*
   GetMenu           kernel   91
   SetMenu           kernel   92

   GetSaveFiles      kernel   93
   GetCWD            kernel   94

/***********************************************
* CheckFreeSpace        kernel      95
************************************************/
      (define SaveGameSize      0)
      (define FreeSpace         1)
      (define EnoughSpaceToSave 2)
/*
   ValidPath         kernel   96
/***********************************************
* CoordPri           kernel      97
************************************************/
      (define PTopOfBand 1)

/*
   StrAt             kernel   98
/***********************************************
* DeviceInfo         kernel      99
************************************************/
      (define GetDevice        0) //sci.sh = diGET_DEVICE
      (define CurDevice        1) //sci.sh = diGET_CURRENT_DEVICE
      (define SameDevice       2) //sci.sh = diPATHS_EQUAL
      (define DevRemovable     3) //sci.sh = diIS_FLOPPY
      (define CloseDevice      4)
      (define SaveDevice       5)
      (define SaveDirMounted   6)
      (define MakeSaveDirName  7)
      (define MakeSaveFileName 8)
/*
   GetSaveDir        kernel   100
   CheckSaveGame     kernel   101

/***********************************************
* ShakeScreen        kernel      102
* shakes [dir]
************************************************/
   (define shakeSDown     1) //sci.sh = ssUPDOWN
   (define shakeSRight    2) //sci.sh = ssLEFTRIGHT
   (define shakeSDiagonal 3) //sci.sh = ssFULL_SHAKE
/*
   FlushResources    kernel   103
   
   SinMult           kernel   104
   CosMult           kernel   105
   SinDiv            kernel   106
   CosDiv            kernel   107

/***********************************************
* Graph              kernel      108
************************************************/
                            // ARGS                        RETURNS
   (define GLoadBits    1)  // bitmap number
   (define GDetect      2)  // no args       RETURNS # of colors available //sci.sh = grGET_COLOURS
   (define GSetPalette  3)  // Palette number
   (define GDrawLine    4)  // x1/y1/x2/y2 mapSet colors... //sci.sh = grDRAW_LINE
   (define GFillArea    5)  // x/y/ mapSet colors...
   (define GDrawBrush   6)  // x/y/ size randomSeed mapSet colors...
   (define GSaveBits    7)  // top/left/bottom/right mapSet            RETURNS saveID of area //sch.sh = grSAVE_BOX
   (define GRestoreBits 8)  // saveID from SaveBits   //sci.sh = grRESTORE_BOX
   (define GEraseRect   9)  // top/left/bottom/right (draws visual in background color) //sci.sh = grFILL_BOX_BACKGROUND
   (define GPaintRect   10) // top/left/bottom/right (draws visual in foreground color) //sci.sh = grFILL_BOX_FOREGROUND
   (define GFillRect    11) // top/left/bottom/right mapSet colors...   //sci.sh = grFILL_BOX
   (define GShowBits    12) // top/left/bottom/right mapSet             //sci.sh = grUPDATE_BOX
   (define GReAnimate   13) // top/left/bottom/right                    //sci.sh = grREDRAW_BOX
   (define GInitPri     14) // horizon/base, Rebuild priority tables    //sci.sh = grADJUST_PRIORITY

/***********************************************
* Joystick              kernel   109
************************************************/
   (define JoyRepeat 12) //sci.sh = jsCALL_DRIVER
/*
   ShiftScreen       kernel   110
/***********************************************
* Palette            kernel      111
************************************************/
   (define PAL_MACTCH  1) // don't steal entries in CLUT
   (define PAL_REPLACE 2) // steal your exact entry in CLUT

   (define PALLoad      1) // palette number and replace/match   //sci.sh = palSET_FROM_RESOURCE
   (define PALSet       2) // first, last, & FLAGS to set        //sci.sh = palSET_FLAG
   (define PALReset     3) // first, last, & FLAGS to reset      //sci.sh = palUNSET_FLAG
   (define PALIntensity 4) // first, last, & intensity (0-100)   //sci.sh = palSET_INTENSITY
   (define PALMatch     5) // Red, Green, Blue (all 0-255), returns index //sci.sh = palFIND_COLOR
   (define PALCycle     6) // first, last, & iterations (negative numbers go in reverse) //sci.sh = palANIMATE
   (define PALSave      7) //sci.sh = palSAVE
   (define PALRestore   8) //sci.sh = palRESTORE

   // defines for FLAGS passed to PALSet, PALReset
   (define  PAL_IN_USE     1)    // this entry is in use
   (define  PAL_NO_MATCH   2)    // never match this color when remapping
   (define  PAL_NO_PURGE   4)    // never overwrite this color when adding a palette
   (define  PAL_NO_REMAP   8)    // never remap this value to another color
   (define  PAL_MATCHED    16)   // in sys pal, shows someone is sharing it

/***********************************************
* MemorySegment         kernel      112
************************************************/
   (define MS_SAVE_FROM 0)
   (define MS_SAVE_TO   1)

   (define  MS_STRING   0) // no length specified - use string length

/***********************************************
* PalVary               kernel   113
* no doco at this time
************************************************/
   (define PALVARYSTART   0) //sci.sh = pvINIT
   (define PALVARYREVERSE 1) //sci.sh = pvREVERSE
   (define PALVARYINFO    2) //sci.sh = pvGET_CURRENT_STEP
   (define PALVARYKILL    3) //sci.sh = pvUNINIT
   (define PALVARYTARGET  4) //sci.sh = pvCHANGE_TARGET
   (define PALVARYNEWTIME 5) //sci.sh = pvCHANGE_TICKS
   (define PALVARYPAUSE   6) //sci.sh = pvPAUSE_RESUME

/***********************************************
* Memory             kernel      114
* Function codes for Memory allocation operations  (Memory)
************************************************/
      (define MNeedPtr    1) //sci.sh = memALLOC_CRIT
      (define MNewPtr     2) //sci.sh = memALLOC_NONCRIT
      (define MDisposePtr 3) //sci.sh = memFREE
      (define MCopy       4) //sci.si = memCOPY
      (define MReadWord   5) //sci.sh = memPEEK
      (define MWriteWord  6) //sci.sh = memPOKE

/***********************************************
* ListOps            kernel   115
* Function codes for list manipulation operations  (ListOps)
************************************************/
      (define LEachElementDo  1)
      (define LFirstTrue      2)
      (define LAllTrue        3)

/***********************************************
* FileIO             kernel   116
************************************************/
   (define fileOpen      0)  //sci.sh = fiOPEN
   (define fileClose     1)  //sci.sh = fiCLOSE
   (define fileRead      2)  //sci.sh = fiREAD
   (define fileWrite     3)  //sci.sh = fiWRITE
   (define fileUnlink    4)  //sci.sh = fiUNLINK
   (define fileFGets     5)  //sci.sh = fiREAD_STRING
   (define fileFPuts     6)  //sci.sh = fiWRITE_STRING
   (define fileSeek      7)  //sci.sh = fiSEEK
   (define fileFindFirst 8)  //sci.sh = fiFIND_FIRST
   (define fileFindNext  9)  //sci.sh = fiFIND_NEXT
   (define fileExists    10) //sci.sh = fiEXISTS
   (define fileRename    11) //sci.sh = fiRENAME
   (define fileCopy      12)

/***********************************************
* DoAudio            kernel   117
************************************************/
   (define WPlay    1)  //sci.sh = audWPLAY
   (define Play     2)  //sci.sh = audPLAY
   (define Stop     3)  //sci.sh = audSTOP
   (define Pause    4)  //sci.sh = audPAUSE
   (define Resume   5)  //sci.sh = audRESUME
   (define Loc      6)  //sci.sh = audPOSITION
   (define Rate     7)  //sci.sh = audRATE
   (define Volume   8)  //sci.sh = audVOLUME
   (define DACFound 9)  //sci.sh = audLANGUAGE
   (define RedBook  10) //sci.sh = audCD
   (define Queue    11)

/***********************************************
* DoSync             kernel   118
************************************************/
   (define StartSync 0) //sci.sh = syncSTART
   (define NextSync  1) //sci.sh = syncNEXT
   (define StopSync  2) //sci.sh = syncSTOP
   (define QueueSync 3)
/*
   AvoidPath         kernel   119
   Sort              kernel   120
   ATan              kernel   121
   Lock              kernel   122

/***********************************************
* ColorRemap            kernel   123
************************************************/
   (define RemapOff       0) // no parameters
   (define RemapByPct     1) // remapColor %intensity [depthOfField]
   (define RemapByRange   2) // remapColor start end increment [depthOfField]
   (define RemapToGray    3) // remapColor %gray [depthOfField]
   (define RemapToPctGray 4) // remapColor %intensity %gray [depthOfField]

/***********************************************
* Message            kernel   124
************************************************/
   (define MsgGet        0) //sci.sh = msgGET
   (define MsgNext       1) //sci.sh = msgNEXT
   (define MsgSize       2) //sci.sh = msgSIZE
   (define MsgGetRefNoun 3) //sci.sh = msgREF_NOUN
   (define MsgGetRefVerb 4) //sci.sh = msgREF_VERB
   (define MsgGetRefCase 5) //sci.sh = msgREF_COND
   (define MsgPush       6) //sci.sh = msgPUSH
   (define MsgPop        7) //sci.sh = msgPOP
   (define MsgGetKey     8) //sci.sh = msgLAST_MESSAGE
/*
   IsItSkip          kernel   125
   MergePoly         kernel   126
   ResCheck          kernel   127
   AssertPalette     kernel   128

   TextColors        kernel   129
   TextFonts         kernel   130

   Record            kernel   131
   PlayBack          kernel   132

   ShowMovie         kernel   133
   SetVideoMode      kernel   134

   SetQuitStr        kernel   135
   // takes near string as only parameter

   DbugStr           kernel   136
   // Parameters:
   //  near string - display to other monitor
   //  1           - set display to first text page
   //  2           - set display to second text page
   //  0           - clear current text page
)
*/