;;; Sierra Script 1.0 - (do not remove this comment)
(script# 990)
(include sci.sh)
(use Main)
(use Intrface)
(use System)

(public
	GetDirectory 0
)

(local
	default
	i
	numGames
	selected
	theStatus
	[butbuf1 4] = [{Restore} {__Save__} {Replace} {Replace}]
	[butbuf2 4] = [{Select the game that you would like to restore.} {Type the description of this saved game.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.}]
)
(procedure (GetDirectory param1 &tmp temp0 [temp1 33] [temp34 40])
	(asm
code_075a:
		pushi    13
		pushi    990
		pushi    1
		pushi    33
		pushi    0
		pushi    41
		pushi    2
		lea      @temp1
		push    
		lsp      param1
		callk    StrCpy,  4
		push    
		pushi    29
		pushi    81
		lofsa    {OK}
		push    
		pushi    1
		pushi    81
		lofsa    {Cancel}
		push    
		pushi    0
		calle    Print,  26
		sat      temp0
		not     
		bnt      code_078d
		ldi      0
		ret     
code_078d:
		pushi    1
		lea      @temp1
		push    
		callk    StrLen,  2
		not     
		bnt      code_07a1
		pushi    1
		lea      @temp1
		push    
		callk    GetCWD,  2
code_07a1:
		pushi    1
		lea      @temp1
		push    
		callk    ValidPath,  2
		bnt      code_07bc
		pushi    2
		lsp      param1
		lea      @temp1
		push    
		callk    StrCpy,  4
		ldi      1
		ret     
		jmp      code_075a
code_07bc:
		pushi    3
		pushi    4
		lea      @temp34
		push    
		pushi    990
		pushi    2
		lea      @temp1
		push    
		callk    Format,  8
		push    
		pushi    33
		pushi    0
		calle    Print,  6
		jmp      code_075a
		ret     
	)
)

(procedure (localproc_04a1)
	(return
		(cond 
			((== self Restore) 0)
			((localproc_07db) 1)
			(numGames 2)
			(else 3)
		)
	)
)

(procedure (localproc_07db)
	(if (< numGames 20) (CheckFreeSpace curSaveDir))
)

(procedure (localproc_07ea)
	(Print 990 3 #font 0)
)

(class SysWindow of Object
	(properties
		top 0
		left 0
		bottom 0
		right 0
		color 0
		back 15
		priority -1
		window 0
		type $0000
		title 0
		brTop 0
		brLeft 0
		brBottom 190
		brRight 320
	)
	
	(method (dispose)
		(if window (DisposeWindow window) (= window 0))
		(super dispose:)
	)
	
	(method (open)
		(= window
			(NewWindow
				top
				left
				bottom
				right
				title
				type
				priority
				color
				back
			)
		)
	)
)

(class SRDialog of Dialog
	(properties
		elements 0
		size 0
		text 0
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		caller 0
		seconds 0
		lastSeconds 0
	)
	
	(method (init param1 param2 param3)
		(= window SysWindow)
		(= nsBottom 0)
		(if
			(==
				(= numGames
					(GetSaveFiles (theGame name?) param2 param3)
				)
				-1
			)
			(return 0)
		)
		(if (== (= theStatus (localproc_04a1)) 1)
			(editI
				text: (StrCpy param1 param2)
				font: smallFont
				setSize:
				moveTo: 4 4
			)
			(self add: editI setSize:)
		)
		(selectorI
			text: param2
			font: smallFont
			setSize:
			moveTo: 4 (+ nsBottom 4)
			state: 2
		)
		(= i (+ (selectorI nsRight?) 4))
		(okI
			text: [butbuf1 theStatus]
			setSize:
			moveTo: i (selectorI nsTop?)
			state: (if (== theStatus 3) 0 else 3)
		)
		(cancelI
			setSize:
			moveTo: i (+ (okI nsBottom?) 4)
			state: (& (cancelI state?) $fff7)
		)
		(changeDirI
			setSize:
			moveTo: i (+ (cancelI nsBottom?) 4)
			state: (& (changeDirI state?) $fff7)
		)
		(self add: selectorI okI cancelI changeDirI setSize:)
		(textI
			text: [butbuf2 theStatus]
			setSize: (- (- nsRight nsLeft) 8)
			moveTo: 4 4
		)
		(= i (+ (textI nsBottom?) 4))
		(self eachElementDo: #move 0 i)
		(self add: textI setSize: center: open: 4 15)
		(return 1)
	)
	
	(method (doit param1 &tmp temp0 temp1 temp2 temp3 [temp4 361] [temp365 21] [temp386 40])
		(asm
			pushSelf
			class    Restore
			eq?     
			bnt      code_026e
			lap      argc
			bnt      code_026e
			lap      param1
			bnt      code_026e
			pushi    2
			pushi    0
			pushi    4
			lea      @temp386
			push    
			pushi    990
			pushi    0
			pushi    #name
			pushi    0
			lag      theGame
			send     4
			push    
			callk    Format,  8
			push    
			callk    FileIO,  4
			sat      temp1
			push    
			ldi      65535
			eq?     
			bnt      code_0267
			ret     
code_0267:
			pushi    2
			pushi    1
			lst      temp1
			callk    FileIO,  4
code_026e:
			pushi    #init
			pushi    3
			lsp      param1
			lea      @temp4
			push    
			lea      @temp365
			push    
			self     10
			not     
			bnt      code_0287
			ldi      65535
			ret     
code_0287:
			lsl      theStatus
			dup     
			ldi      0
			eq?     
			bnt      code_02a1
			lal      numGames
			bnt      code_02be
			lofsa    okI
			jmp      code_02be
			lofsa    changeDirI
			jmp      code_02be
code_02a1:
			dup     
			ldi      1
			eq?     
			bnt      code_02ae
			lofsa    editI
			jmp      code_02be
code_02ae:
			dup     
			ldi      2
			eq?     
			bnt      code_02bb
			lofsa    okI
			jmp      code_02be
code_02bb:
			lofsa    changeDirI
code_02be:
			toss    
			sal      default
			pushi    #doit
			pushi    1
			push    
			super    Dialog,  6
			sal      i
			pushi    #indexOf
			pushi    1
			pushi    #cursor
			pushi    0
			lofsa    selectorI
			send     4
			push    
			lofsa    selectorI
			send     6
			sal      selected
			push    
			ldi      18
			mul     
			sat      temp3
			lsl      i
			lofsa    changeDirI
			eq?     
			bnt      code_0389
			pushi    1
			lsg      curSaveDir
			call     GetDirectory,  2
			bnt      code_0287
			pushi    3
			pushi    #name
			pushi    0
			lag      theGame
			send     4
			push    
			lea      @temp4
			push    
			lea      @temp365
			push    
			callk    GetSaveFiles,  6
			sal      numGames
			push    
			ldi      65535
			eq?     
			bnt      code_031d
			ldi      65535
			sat      temp2
			jmp      code_0499
code_031d:
			lal      theStatus
			sat      temp0
			pushi    0
			call     localproc_04a1,  0
			sal      theStatus
			push    
			dup     
			ldi      0
			eq?     
			bnt      code_0333
			jmp      code_0379
code_0333:
			dup     
			lat      temp0
			eq?     
			bnt      code_0364
			pushi    #contains
			pushi    1
			lofsa    editI
			push    
			self     6
			bnt      code_0379
			pushi    #cursor
			pushi    1
			pushi    1
			pushi    2
			lsp      param1
			lea      @temp4
			push    
			callk    StrCpy,  4
			push    
			callk    StrLen,  2
			push    
			pushi    83
			pushi    0
			lofsa    editI
			send     10
			jmp      code_0379
code_0364:
			pushi    #dispose
			pushi    0
			pushi    102
			pushi    3
			lsp      param1
			lea      @temp4
			push    
			lea      @temp365
			push    
			self     14
code_0379:
			toss    
			pushi    #setSize
			pushi    0
			pushi    83
			pushi    0
			lofsa    selectorI
			send     8
			jmp      code_0287
code_0389:
			lsl      theStatus
			ldi      2
			eq?     
			bnt      code_03bf
			lsl      i
			lofsa    okI
			eq?     
			bnt      code_03bf
			pushi    #doit
			pushi    1
			pushi    2
			lsp      param1
			lat      temp3
			leai     @temp4
			push    
			callk    StrCpy,  4
			push    
			lofsa    GetReplaceName
			send     6
			bnt      code_0287
			lal      selected
			lati     temp365
			sat      temp2
			jmp      code_0499
			jmp      code_0287
code_03bf:
			lsl      theStatus
			ldi      1
			eq?     
			bnt      code_0440
			lsl      i
			lofsa    okI
			eq?     
			bt       code_03d9
			lsl      i
			lofsa    editI
			eq?     
			bnt      code_0440
code_03d9:
			pushi    1
			lsp      param1
			callk    StrLen,  2
			push    
			ldi      0
			eq?     
			bnt      code_03ee
			pushi    0
			call     localproc_07ea,  0
			jmp      code_0287
code_03ee:
			ldi      65535
			sat      temp2
			ldi      0
			sal      i
code_03f6:
			lsl      i
			lal      numGames
			lt?     
			bnt      code_0418
			pushi    2
			lsp      param1
			lsl      i
			ldi      18
			mul     
			leai     @temp4
			push    
			callk    StrCmp,  4
			sat      temp2
			not     
			bnt      code_0413
code_0413:
			+al      i
			jmp      code_03f6
code_0418:
			lat      temp2
			not     
			bnt      code_0426
			lal      i
			lati     temp365
			jmp      code_0438
code_0426:
			lsl      numGames
			ldi      20
			eq?     
			bnt      code_0436
			lal      selected
			lati     temp365
			jmp      code_0438
code_0436:
			lal      numGames
code_0438:
			sat      temp2
			jmp      code_0499
			jmp      code_0287
code_0440:
			lsl      i
			lofsa    okI
			eq?     
			bnt      code_0456
			lal      selected
			lati     temp365
			sat      temp2
			jmp      code_0499
			jmp      code_0287
code_0456:
			lsl      i
			ldi      0
			eq?     
			bt       code_0467
			lsl      i
			lofsa    cancelI
			eq?     
			bnt      code_0471
code_0467:
			ldi      65535
			sat      temp2
			jmp      code_0499
			jmp      code_0287
code_0471:
			lsl      theStatus
			ldi      1
			eq?     
			bnt      code_0287
			pushi    #cursor
			pushi    1
			pushi    1
			pushi    2
			lsp      param1
			lat      temp3
			leai     @temp4
			push    
			callk    StrCpy,  4
			push    
			callk    StrLen,  2
			push    
			pushi    83
			pushi    0
			lofsa    editI
			send     10
			jmp      code_0287
code_0499:
			pushi    #dispose
			pushi    0
			self     4
			lat      temp2
			ret     
		)
	)
)

(class Restore of SRDialog
	(properties
		elements 0
		size 0
		text {Restore a Game}
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		caller 0
		seconds 0
		lastSeconds 0
	)
)

(class Save of SRDialog
	(properties
		elements 0
		size 0
		text {Save a Game}
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		caller 0
		seconds 0
		lastSeconds 0
	)
)

(instance GetReplaceName of Dialog
	(properties)
	
	(method (doit param1 &tmp temp0)
		(= window SysWindow)
		(text1 setSize: moveTo: 4 4)
		(self add: text1 setSize:)
		(oldName
			text: param1
			font: smallFont
			setSize:
			moveTo: 4 nsBottom
		)
		(self add: oldName setSize:)
		(text2 setSize: moveTo: 4 nsBottom)
		(self add: text2 setSize:)
		(newName
			text: param1
			font: smallFont
			setSize:
			moveTo: 4 nsBottom
		)
		(self add: newName setSize:)
		(button1 nsLeft: 0 nsTop: 0 setSize:)
		(button2 nsLeft: 0 nsTop: 0 setSize:)
		(button2
			moveTo: (- nsRight (+ (button2 nsRight?) 4)) nsBottom
		)
		(button1
			moveTo: (- (button2 nsLeft?) (+ (button1 nsRight?) 4)) nsBottom
		)
		(self add: button1 button2 setSize: center: open: 0 15)
		(= temp0 (super doit: newName))
		(self dispose:)
		(if (not (StrLen param1))
			(localproc_07ea)
			(= temp0 0)
		)
		(return (if (== temp0 newName) else (== temp0 button1)))
	)
)

(instance selectorI of DSelector
	(properties
		x 36
		y 8
	)
)

(instance editI of DEdit
	(properties
		max 35
	)
)

(instance okI of DButton
	(properties)
)

(instance cancelI of DButton
	(properties
		text { Cancel_}
	)
)

(instance changeDirI of DButton
	(properties
		text {Change\0D\nDirectory}
	)
)

(instance textI of DText
	(properties
		font 0
	)
)

(instance text1 of DText
	(properties
		text {Replace}
		font 0
	)
)

(instance text2 of DText
	(properties
		text {with:}
		font 0
	)
)

(instance oldName of DText
	(properties)
)

(instance newName of DEdit
	(properties
		max 35
	)
)

(instance button1 of DButton
	(properties
		text {Replace}
	)
)

(instance button2 of DButton
	(properties
		text {Cancel}
	)
)
