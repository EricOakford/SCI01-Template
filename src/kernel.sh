;;; Sierra Script 1.0 - (do not remove this comment)
;KERNEL.SH
;Definitions of the kernel interface for the Script interpreter.

(define  kernel   $ffff)

;These External Defines are based on the SCI1 kernels. SCI0 and SCI01 may have different kernal reference numbers.

;(extern
;   ;Resource handling.
;   Load              kernel   00
;   UnLoad            kernel   01
;   ScriptID          kernel   02
;   DisposeScript     kernel   03
;
;   ;Object management.
;   Clone             kernel   04
;   DisposeClone      kernel   05
;   IsObject          kernel   06
;   RespondsTo        kernel   07
;
;   ;Pictures.
;   DrawPic           kernel   08
;   Show              kernel   09
;   PicNotValid       kernel   10
;
;   ;Animated objects & views.
;   Animate           kernel   11
;   SetNowSeen        kernel   12
;   NumLoops          kernel   13 ; view
;   NumCels           kernel   14 ; view/loop
;   CelWide           kernel   15 ; view/loop/cel
;   CelHigh           kernel   16 ; view/loop/cel
;   DrawCel           kernel   17 ; view/loop/cel/left/top/priority/palette
;   AddToPic          kernel   18
;
;   ;Window/dialog/controls.
;   NewWindow         kernel   19
;   GetPort           kernel   20
;   SetPort           kernel   21
;; SetPortRect       kernel   21    ;this is the same kernel call as SetPort
;   DisposeWindow     kernel   22
;   DrawControl       kernel   23
;   HiliteControl     kernel   24
;   EditControl       kernel   25
;
;   ;Screen text.
;   TextSize          kernel   26
;   Display           kernel   27
;
;   ;Events.
;   GetEvent          kernel   28
;   GlobalToLocal     kernel   29
;   LocalToGlobal     kernel   30
;   MapKeyToDir       kernel   31
;
;   ;Menu bar & status line.
;   DrawMenuBar       kernel   32
;   MenuSelect        kernel   33
;   AddMenu           kernel   34
;   DrawStatus        kernel   35
;
;   ;Parsing.
;; Parse             kernel   36
;; Said              kernel   37
;; SetSynonyms       kernel   38
;
;   ;Mouse functions.
;   HaveMouse         kernel   39
;   SetCursor         kernel   40
;
;
;   ;Save/restore/restart.
;   SaveGame          kernel   41
;   RestoreGame       kernel   42
;   RestartGame       kernel   43
;   GameIsRestarting  kernel   44
;
;   ;Sounds.
;  DoSound in SCI0         kernel   45
;   (define InitSound		0)  ;sci.sh = sndINIT
;   (define PlaySound		1)  ;sci.sh = sndPLAY
;   (define NextSound		2)  ;sci.sh = sndNOP
;   (define KillSound		3)  ;sci.sh = sndDISPOSE
;   (define SoundOn		4)      ;sci.sh = sndSET_SOUND ;NOTE: This definition changed between SCI0 and SCI1.1
;   (define StopSound		5)  ;sci.sh = sndSTOP
;   (define PauseSound		6)  ;sci.sh = sndPAUSE
;   (define RestoreSound	7)  ;sci.sh = sndRESUME
   (define ChangeVolume		8)  ;sci.sh = sndVOLUME
;   (define ChangeSndState	9)  ;sci.sh = sndUPDATE
;   (define FadeSound		10) ;sci.sh = sndFADE
;   (define NumVoices		11) ;sci.sh = sndCHECK_DRIVER
;   (define (unknown)		12) ;sci.sh = sndSTOP_ALL


;  DoSound in SCI1.1       kernel   45
   (define MasterVol    0)  ;sci.sh = sndMASTER_VOLUME           SCI0 = sndINIT
   (define SoundOn      1)  ;sci.sh = sndSET_SOUND               SCI0 = sndPLAY
   (define RestoreSound 2)  ;sci.sh = sndRESTORE                 SCI0 = sndNOP
   (define NumVoices    3)  ;sci.sh = sndGET_POLYPHONY           SCI0 = sndDISPOSE
   (define NumDACs      4)  ;sci.sh = sndGET_AUDIO_CAPABILITY    SCI0 = sndSET_SOUND
   (define Suspend      5)  ;sci.sh = sndSUSPEND                 SCI0 = sndSTOP
   (define InitSound    6)  ;sci.sh = sndINIT                    SCI0 = sndPAUSE
   (define KillSound    7)  ;sci.sh = sndDISPOSE                 SCI0 = sndRESUME
   (define PlaySound    8)  ;sci.sh = sndPLAY                    SCI0 = sndVOLUME
   (define StopSound    9)  ;sci.sh = sndSTOP                    SCI0 = sndUPDATE
   (define PauseSound   10) ;sci.sh = sndPAUSE                   SCI0 = sndFADE
   (define FadeSound    11) ;sci.sh = sndFADE                    SCI0 = sndCHECK_DRIVER
   (define HoldSound    12) ;sci.sh = sndSET_HOLD                SCI0 = sndSTOP_ALL
   (define MuteSound    13) ;sci.sh = sndDUMMY
   (define SetVol       14) ;sci.sh = sndSET_VOLUME
   (define SetPri       15) ;sci.sh = sndSET_PRIORITY
   (define SetLoop      16) ;sci.sh = sndSET_LOOP
   (define UpdateCues   17) ;sci.sh = sndUPDATE_CUES
   (define MidiSend     18) ;sci.sh = sndSEND_MIDI
   (define SetReverb    19) ;sci.sh = sndGLOBAL_REVERB

   ; This will be removed after KQ5 cd and
   ; Jones cd are shipped (DO NOT USE)
   (define ChangeSndState 20) ;sci.sh = sndUPDATE


;   ;List handling.
;   NewList           kernel   46
;   DisposeList       kernel   47
;   NewNode           kernel   48
;   FirstNode         kernel   49
;   LastNode          kernel   50
;   EmptyList         kernel   51
;   NextNode          kernel   52
;   PrevNode          kernel   53
;   NodeValue         kernel   54
;   AddAfter          kernel   55
;   AddToFront        kernel   56
;   AddToEnd          kernel   57
;   FindKey           kernel   58
;   DeleteKey         kernel   59

;   ;Mathematical functions.
;   Random            kernel   60
;   Abs               kernel   61
;   Sqrt              kernel   62
;   GetAngle          kernel   63
;   GetDistance       kernel   64

;   ;Miscellaneous.
;   Wait              kernel   65
;   GetTime           kernel   66
      ; pass NO argument for 60ths second "ticks" value
      (enum 1  ; 0 is undefined in SysTime                                    ;sci.sh = gtTIME_ELAPSED = 0
         SYSTIME1    ; returns HHHH|MMMM|MMSS|SSSS (1 sec resoulution, 12 Hr) ;sci.sh = gtTIME_OF_DAY = 1
         SYSTIME2    ; returns HHHH|HMMM|MMMS|SSSS (2 sec resoulution 24 Hr)
         SYSDATE     ; returns YYYY|YYYM|MMMD|DDDD (date since 1980)  
      )

   ;String handling.
;   StrEnd            kernel   67
;   StrCat            kernel   68
;   StrCmp            kernel   69
;   StrLen            kernel   70
;   StrCpy            kernel   71
;   Format            kernel   72
;   GetFarText        kernel   73
;   ReadNumber        kernel   74

;   ;Actor motion support.
;   BaseSetter        kernel   75
;   DirLoop           kernel   76
;   CantBeHere        kernel   77
;   OnControl         kernel   78
;   InitBresen        kernel   79
;   DoBresen          kernel   80
;   Platform          kernel   81
;   SetJump           kernel   82

;   ;Debugging support.
;   SetDebug          kernel   83
;   InspectObj        kernel   84
;   ShowSends         kernel   85
;; ShowObjs          kernel   86
;   ShowFree          kernel   87
;   MemoryInfo        kernel   88
      (enum
         LargestPtr        ;sci.sh = miFREEHEAP
         FreeHeap          ;sci.sh = miLARGESTPTR
         LargestHandle     ;sci.sh = miLARGESTHUNK
         FreeHunk          ;sci.sh = miFREEHUNK
         TotalHunk         ;sci.sh = <undefined>
      )
;   StackUsage        kernel   89
      (enum
         PStackSize
         PStackMax
         PStackCur
         MStackSize
         MStackMax
         MStackCur
      )
;   Profiler          kernel   90
      (enum
         PRO_OPEN
         PRO_CLOSE
         PRO_ON
         PRO_OFF
         PRO_CLEAR
         PRO_REPORT
         TRACE_ON
         TRACE_OFF
         TRACE_RPT
      )
;   GetMenu           kernel   91
;   SetMenu           kernel   92

;   GetSaveFiles      kernel   93
;   GetCWD            kernel   94

;   CheckFreeSpace    kernel   95
      (enum
         SaveGameSize
         FreeSpace
         EnoughSpaceToSave
      )

;   ValidPath         kernel   96

;   CoordPri          kernel   97
      (define PTopOfBand 1)
;   StrAt             kernel   98
;   DeviceInfo        kernel   99
      (enum
         GetDevice    ;sci.sh = diGET_DEVICE
         CurDevice    ;sci.sh = diGET_CURRENT_DEVICE
         SameDevice   ;sci.sh = diPATHS_EQUAL
         DevRemovable ;sci.sh = diIS_FLOPPY
         CloseDevice
         SaveDevice
         SaveDirMounted 
         MakeSaveDirName
         MakeSaveFileName
         )
 ;  GetSaveDir        kernel   100
 ;  CheckSaveGame     kernel   101

;   ShakeScreen       kernel   102
      ; shakes [dir]
      (enum 1
         shakeSDown     ;sci.sh = ssUPDOWN
         shakeSRight    ;sci.sh = ssLEFTRIGHT
         shakeSDiagonal ;sci.sh = ssFULL_SHAKE
      )


;   FlushResources    kernel   103
   
;   SinMult           kernel   104
;   CosMult           kernel   105
;   SinDiv            kernel   106
;   CosDiv            kernel   107

;   Graph             kernel   108
      (enum 1           ; ARGS                        RETURNS
         GLoadBits      ; bitmap number
         GDetect        ; none                        # of colors available        ;sci.sh = grGET_COLOURS

         GSetPalette    ; Palette number
         GDrawLine      ; x1/y1/x2/y2 mapSet colors...
         GFillArea      ; x/y/ mapSet colors...
         GDrawBrush     ; x/y/ size randomSeed mapSet colors...                    ;sci.sh = grDRAW_LINE
         GSaveBits      ; top/left/bottom/right mapSet            saveID of area   ;sci.sh = grSAVE_BOX
         GRestoreBits   ; saveID from SaveBits                                     ;sci.sh = grRESTORE_BOX
         GEraseRect     ; top/left/bottom/right (draws visual in background color) ;sci.sh = grFILL_BOX_BACKGROUND
         GPaintRect     ; top/left/bottom/right (draws visual in foreground color) ;sci.sh = grFILL_BOX_FOREGROUND
         GFillRect      ; top/left/bottom/right mapSet colors...                   ;sci.sh = grFILL_BOX
         GShowBits      ; top/left/bottom/right mapSet                             ;sci.sh = grUPDATE_BOX
         GReAnimate     ; top/left/bottom/right                                    ;sci.sh = grREDRAW_BOX
         GInitPri       ; horizon/base, Rebuild priority tables                    ;sci.sh = grADJUST_PRIORITY
      )
;
;   Joystick          kernel   109
      (enum 12
         JoyRepeat      ;sci.sh = jsCALL_DRIVER
      )

;   ShiftScreen       kernel   110

;   Palette           kernel   111

      (enum 1
         PAL_MATCH      ; don't steal entries in CLUT
         PAL_REPLACE    ; steal your exact entry in CLUT
      )
      (enum 1
         PALLoad        ; palette number and replace/match                               ;sci.sh = palSET_FROM_RESOURCE
         PALSet         ; first, last, & FLAGS to set                                    ;sci.sh = palSET_FLAG
         PALReset       ; first, last, & FLAGS to reset                                  ;sci.sh = palUNSET_FLAG
         PALIntensity   ; first, last, & intensity (0-100)                               ;sci.sh = palSET_INTENSITY
         PALMatch       ; Red, Green, Blue (all 0-255), returns index                    ;sci.sh = palFIND_COLOR
         PALCycle       ; first, last, & iterations (negative numbers go in reverse)     ;sci.sh = palANIMATE
         PALSave                                                                         ;sci.sh = palSAVE
         PALRestore                                                                      ;sci.sh = palRESTORE
      )
      ; defines for FLAGS passed to PALSet, PALReset
      (define  PAL_IN_USE     1)    ; this entry is in use
      (define  PAL_NO_MATCH   2)    ; never match this color when remapping
      (define  PAL_NO_PURGE   4)    ; never overwrite this color when adding a palette
      (define  PAL_NO_REMAP   8)    ; never remap this value to another color
      (define  PAL_MATCHED    16)   ; in sys pal, shows someone is sharing it

 ;  MemorySegment     kernel   112
      (enum
         MS_SAVE_FROM
         MS_RESTORE_TO
      )
      (define  MS_STRING   0) ; no length specified - use string length

 ;  PalVary           kernel   113
      ; no doco at this time
      (enum
         PALVARYSTART         ;sci.sh = pvINIT
         PALVARYREVERSE       ;sci.sh = pvREVERSE
         PALVARYINFO          ;sci.sh = pvGET_CURRENT_STEP
         PALVARYKILL          ;sci.sh = pvUNINIT
         PALVARYTARGET        ;sci.sh = pvCHANGE_TARGET
         PALVARYNEWTIME       ;sci.sh = pvCHANGE_TICKS
         PALVARYPAUSE         ;sci.sh = pvPAUSE_RESUME
      )

;   Memory            kernel   114
      ; Function codes for Memory allocation operations  (Memory)
      (enum 1
         MNeedPtr             ;sci.sh = memALLOC_CRIT
         MNewPtr              ;sci.sh = memALLOC_NONCRIT
         MDisposePtr          ;sci.sh = memFREE
         MCopy                ;sci.sh = memCOPY
         MReadWord            ;sci.sh = memPEEK
         MWriteWord           ;sci.sh = memPOKE
      )
;   ListOps           kernel   115
      ; Function codes for list manipulation operations  (ListOps)
      (enum 1
         LEachElementDo 
         LFirstTrue    
         LAllTrue      
      )
;   FileIO            kernel   116
      (enum
         fileOpen             ;sci.sh = fiOPEN
         fileClose            ;sci.sh = fiCLOSE
         fileRead             ;sci.sh = fiREAD
         fileWrite            ;sci.sh = fiWRITE
         fileUnlink           ;sci.sh = fiUNLINK
         fileFGets            ;sci.sh = fiREAD_STRING
         fileFPuts            ;sci.sh = fiWRITE_STRING
         fileSeek             ;sci.sh = fiSEEK
         fileFindFirst        ;sci.sh = fiFIND_FIRST
         fileFindNext         ;sci.sh = fiFIND_NEXT
         fileExists           ;sci.sh = fiEXISTS
         fileRename           ;sci.sh = fiRENAME
         fileCopy
      )
;   DoAudio           kernel   117
      (enum 1
         WPlay          ;sci.sh = audWPLAY
         Play           ;sci.sh = audPLAY
         Stop           ;sci.sh = audSTOP
         Pause          ;sci.sh = audPAUSE
         Resume         ;sci.sh = audRESUME
         Loc            ;sci.sh = audPOSITION
         Rate           ;sci.sh = audRATE
         Volume         ;sci.sh = audVOLUME
         DACFound       ;sci.sh = audLANGUAGE
         RedBook        ;sci.sh = audCD
         Queue
      )
;   DoSync            kernel   118
      (enum 
         StartSync  ;sci.sh = syncSTART
         NextSync   ;sci.sh = syncNEXT
         StopSync   ;sci.sh = syncSTOP
         QueueSync
      )
;   AvoidPath         kernel   119
;   Sort              kernel   120
;   ATan              kernel   121
;   Lock              kernel   122
;   ColorRemap        kernel   123
      (enum
         RemapOff       ; no parameters
         RemapByPct     ; remapColor %intensity [depthOfField]
         RemapByRange   ; remapColor start end increment [depthOfField]
         RemapToGray    ; remapColor %gray [depthOfField]
         RemapToPctGray ; remapColor %intensity %gray [depthOfField]
      )
;   Message           kernel   124
      (enum 
         MsgGet             ;sci.sh = msgGET
         MsgNext            ;sci.sh = msgNEXT
         MsgSize            ;sci.sh = msgSIZE
         MsgGetRefNoun      ;sci.sh = msgREF_NOUN
         MsgGetRefVerb      ;sci.sh = msgREF_VERB
         MsgGetRefCase      ;sci.sh = msgREF_COND
         MsgPush            ;sci.sh = msgPUSH
         MsgPop             ;sci.sh = msgPOP
         MsgGetKey          ;sci.sh = msgLAST_MESSAGE
      )

;   IsItSkip          kernel   125
;   MergePoly         kernel   126
;   ResCheck          kernel   127
;   AssertPalette     kernel   128

;   TextColors        kernel   129
;   TextFonts         kernel   130

;   Record            kernel   131
;   PlayBack          kernel   132

;   ShowMovie         kernel   133
;   SetVideoMode      kernel   134

;   SetQuitStr        kernel   135
   ; takes near string as only parameter

;   DbugStr           kernel   136
   ; Parameters:
   ;  near string - display to other monitor
   ;  1           - set display to first text page
   ;  2           - set display to second text page
   ;  0           - clear current text page
;)
