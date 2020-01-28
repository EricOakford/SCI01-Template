;;; Sierra Script 1.0 - (do not remove this comment)
(script# PATHER)
(include game.sh)
(use Main)
(use Intrface)
(use Polygon)
(use Save)
(use File)
(use User)
(use System)

(define	BEGINPATH		$c800)
(define	BEGINCLOSED		$e800)
(define	ENDPATH			$8000)
(define	CLEARBITS		$03ff)


(local
	pathType
	vSaveBits
	cSaveBits
	index
	curPathStart
	lastX =  -1
	lastY
	curMap
	[theFile 15]
	[pathHdr 50]
	[pathPts 500]
	[local573 80]
	x1
	y1
	x2
	y2
	local657
	local658
	local659
	local660
	local661
	local662
	[local663 3] = [{PTotalAccess} {PNearestAccess} {PBarredAccess}]
)
(procedure (SetVars v1 v2 v3 v4)
	(if (> v1 v3)
		(= y1 v3)
		(= y2 v1)
	else
		(= y1 v1)
		(= y2 v3)
	)
	(if (> v2 v4)
		(= x1 v4)
		(= x2 v2)
	else
		(= x1 v2)
		(= x2 v4)
	)
	(-- x1)
	(-- y1)
	(++ x2)
	(return (++ y2))
)

(procedure (LogIt what)
	(File name: @theFile writeString: what close:)
	(DisposeScript FILE)
)

(class MakePath
	(properties)
	
	(method (doit &tmp temp0 temp1 temp2 temp3)
		(asm
			pushi    #add
			pushi    1
			pushSelf
			lag      features
			send     6
			ldi      0
			sat      temp3
			ldi      1
			sal      local657
			sal      local660
			pushi    #signal
			pushi    0
			lag      ego
			send     4
			push    
			ldi      128
			and     
			sal      local661
			pushi    #show
			pushi    0
			lag      ego
			send     4
			pushi    10
			pushi    PATHER
			pushi    0
			pushi    80
			lofsa    {Path Maker v1.2}
			push    
			pushi    81
			lofsa    {PolyPath}
			push    
			pushi    1
			pushi    81
			lofsa    {Freeway}
			push    
			pushi    2
			calle    Print,  20
			sal      pathType
			not     
			bnt      code_0056
			ret     
code_0056:
			pushi    4
			lea      @theFile
			push    
			pushi    PATHER
			pushi    1
			pushi    #picture
			pushi    0
			lag      curRoom
			send     4
			push    
			callk    Format,  8
			pushi    3
			lea      @theFile
			push    
			pushi    30
			lofsa    {Enter path and filename}
			push    
			calle    GetInput,  6
			pushi    4
			pushi    #curPic
			pushi    0
			lag      curRoom
			send     4
			push    
			pushi    100
			pushi    1
			lsg      currentPalette
			callk    DrawPic,  8
			lsg      overlays
			ldi      65535
			ne?     
			bnt      code_00a3
			pushi    4
			lsg      overlays
			pushi    100
			pushi    0
			lsg      currentPalette
			callk    DrawPic,  8
code_00a3:
			pushi    #doit
			pushi    0
			lag      addToPics
			send     4
			pushi    1
			ldi      1
			sal      curMap
			push    
			callk    Show,  2
			ldi      0
			sal      index
			sal      curPathStart
			pushi    #obstacles
			pushi    0
			lag      curRoom
			send     4
			bnt      code_0104
			lsl      pathType
			ldi      1
			eq?     
			bnt      code_0104
			pushi    10
			pushi    PATHER
			pushi    2
			pushi    80
			lofsa    {Path Maker v1.2}
			push    
			pushi    81
			lofsa    {YES}
			push    
			pushi    1
			pushi    81
			lofsa    {NO}
			push    
			pushi    0
			calle    Print,  20
			bnt      code_0104
			lsl      pathType
			ldi      2
			eq?     
			bnt      code_00fe
			pushi    #getFreeway
			pushi    0
			self     4
			jmp      code_0104
code_00fe:
			pushi    #getPoly
			pushi    0
			self     4
code_0104:
			pushi    #illegalBits
			pushi    0
			lag      ego
			send     4
			sal      local658
			pushi    #setLoop
			pushi    1
			pushi    2
			pushi    242
			pushi    1
			pushi    0
			pushi    18
			pushi    1
			pushi    0
			lag      ego
			send     18
			pushi    65535
			lal      index
			sali     pathPts
code_0126:
			lat      temp3
			not     
			bnt      code_0600
			pushi    1
			pushi    0
			callk    SetPort,  2
			pushi    #new
			pushi    0
			class    Event
			send     4
			sat      temp0
			pushi    3
			pushi    #superClass
			pushi    #y
			pushi    0
			send     4
			push    
			ldi      10
			sub     
			push    
			lat      temp0
			send     6
			pushi    #y
			pushi    0
			lat      temp0
			send     4
			push    
			ldi      0
			lt?     
			bnt      code_0161
			pushi    #y
			pushi    1
			pushi    0
			lat      temp0
			send     6
code_0161:
			pushi    #x
			pushi    0
			lag      ego
			send     4
			push    
			pushi    #x
			pushi    0
			lat      temp0
			send     4
			ne?     
			bt       code_018e
			pushi    #y
			pushi    0
			lag      ego
			send     4
			push    
			pushi    #y
			pushi    0
			lat      temp0
			send     4
			ne?     
			bt       code_018e
			pushi    #type
			pushi    0
			lat      temp0
			send     4
code_018e:
			sal      local662
			bnt      code_0273
			pushi    #posn
			pushi    2
			pushi    #x
			pushi    0
			lat      temp0
			send     4
			push    
			pushi    #y
			pushi    0
			lat      temp0
			send     4
			push    
			lag      ego
			send     8
			pushi    2
			pushi    #elements
			pushi    0
			lag      cast
			send     4
			push    
			pushi    0
			callk    Animate,  4
			pushi    #onControl
			pushi    0
			lag      ego
			send     4
			sat      temp1
			pushi    9
			pushi    PATHER
			pushi    3
			pushi    100
			pushi    3
			pushi    1
			pushi    106
			pushi    0
			pushi    103
			pushi    3
			callk    Display,  18
			lst      temp1
			ldi      1
			shr     
			sat      temp1
			ldi      1
			sat      temp2
code_01e4:
			lst      temp2
			ldi      16
			lt?     
			bnt      code_0226
			lst      temp1
			ldi      1
			and     
			bnt      code_0209
			pushi    6
			pushi    PATHER
			pushi    4
			pushi    106
			pushi    6
			pushi    103
			lst      temp2
			callk    Display,  12
			jmp      code_021a
code_0209:
			pushi    6
			pushi    PATHER
			pushi    4
			pushi    106
			pushi    6
			pushi    103
			pushi    0
			callk    Display,  12
code_021a:
			lst      temp1
			ldi      1
			shr     
			sat      temp1
			+at      temp2
			jmp      code_01e4
code_0226:
			lag      modelessDialog
			bnt      code_0230
			pushi    #dispose
			pushi    0
			send     4
code_0230:
			lal      vSaveBits
			bnt      code_0259
			pushi    2
			pushi    8
			push    
			callk    Graph,  4
			pushi    2
			pushi    8
			lsl      cSaveBits
			callk    Graph,  4
			pushi    6
			pushi    12
			lsl      y1
			lsl      x1
			lsl      y2
			lsl      x2
			lsl      curMap
			callk    Graph,  12
code_0259:
			pushi    4
			pushi    #y
			pushi    0
			lag      ego
			send     4
			push    
			pushi    #x
			pushi    0
			lag      ego
			send     4
			push    
			lsl      lastY
			lsl      lastX
			call     SetVars,  8
code_0273:
			pushi    #type
			pushi    0
			lat      temp0
			send     4
			push    
			ldi      1
			eq?     
			bnt      code_039e
			pushi    #modifiers
			pushi    0
			lat      temp0
			send     4
			push    
			ldi      4
			and     
			bnt      code_039e
			lsl      index
			lal      curPathStart
			gt?     
			bnt      code_039e
			lsl      pathType
			ldi      1
			eq?     
			bnt      code_037c
			lal      curPathStart
			lsli     pathPts
			ldi      1023
			and     
			push    
			lal      index
			sali     pathPts
			lsl      curPathStart
			ldi      1
			add     
			lsli     pathPts
			ldi      32768
			or      
			push    
			+al      index
			sali     pathPts
			pushi    8
			pushi    4
			lsl      index
			ldi      2
			sub     
			lsli     pathPts
			ldi      1023
			and     
			push    
			lsl      index
			ldi      3
			sub     
			lsli     pathPts
			ldi      1023
			and     
			push    
			lal      index
			lsli     pathPts
			ldi      1023
			and     
			push    
			lsl      index
			ldi      1
			sub     
			lsli     pathPts
			ldi      1023
			and     
			push    
			pushi    0
			pushi    65535
			pushi    10
			callk    Graph,  16
			pushi    4
			lsl      index
			ldi      2
			sub     
			lsli     pathPts
			ldi      1023
			and     
			push    
			lsl      index
			ldi      3
			sub     
			lsli     pathPts
			ldi      1023
			and     
			push    
			lal      index
			lsli     pathPts
			ldi      1023
			and     
			push    
			lsl      index
			ldi      1
			sub     
			lsli     pathPts
			ldi      1023
			and     
			push    
			call     SetVars,  8
			pushi    3
			lea      @local573
			push    
			pushi    PATHER
			pushi    5
			callk    Format,  6
			pushi    6
			pushi    12
			lsl      y1
			lsl      x1
			lsl      y2
			lsl      x2
			lsl      curMap
			callk    Graph,  12
			pushi    13
			pushi    PATHER
			pushi    6
			pushi    80
			lofsa    {Path Maker v1.2}
			push    
			pushi    81
			lofsa    {Total}
			push    
			pushi    0
			pushi    81
			lofsa    {Near}
			push    
			pushi    1
			pushi    81
			lofsa    {Barred}
			push    
			pushi    2
			calle    Print,  26
			push    
			+al      local659
			sali     pathHdr
			jmp      code_0389
code_037c:
			-al      index
			lsli     pathPts
			ldi      32768
			or      
			push    
			lal      index
			sali     pathPts
code_0389:
			pushi    65535
			+al      index
			sali     pathPts
			lal      index
			sal      curPathStart
			ldi      65535
			sal      lastX
			ldi      0
			sal      vSaveBits
			jmp      code_055c
code_039e:
			pushi    #type
			pushi    0
			lat      temp0
			send     4
			push    
			ldi      1
			eq?     
			bnt      code_0420
			lsl      lastX
			ldi      65535
			eq?     
			bnt      code_03ca
			pushi    #x
			pushi    0
			lag      ego
			send     4
			sal      lastX
			push    
			ldi      51200
			or      
			push    
			lal      index
			sali     pathPts
			jmp      code_040d
code_03ca:
			pushi    8
			pushi    4
			pushi    #y
			pushi    0
			lag      ego
			send     4
			push    
			pushi    #x
			pushi    0
			lag      ego
			send     4
			push    
			lsl      lastY
			lsl      lastX
			pushi    0
			pushi    65535
			pushi    10
			callk    Graph,  16
			pushi    6
			pushi    12
			lsl      y1
			lsl      x1
			lsl      y2
			lsl      x2
			lsl      curMap
			callk    Graph,  12
			pushi    #x
			pushi    0
			lag      ego
			send     4
			sal      lastX
			push    
			lal      index
			sali     pathPts
code_040d:
			pushi    #y
			pushi    0
			lag      ego
			send     4
			sal      lastY
			push    
			+al      index
			sali     pathPts
			+al      index
			jmp      code_055c
code_0420:
			pushi    #type
			pushi    0
			lat      temp0
			send     4
			push    
			ldi      4
			eq?     
			bnt      code_055c
			pushi    #message
			pushi    0
			lat      temp0
			send     4
			push    
			dup     
			ldi      19
			eq?     
			bnt      code_044a
			ldi      1
			sat      temp3
			pushi    65535
			lal      index
			sali     pathPts
			jmp      code_055b
code_044a:
			dup     
			ldi      12800
			eq?     
			bnt      code_0484
			lal      vSaveBits
			bnt      code_047b
			pushi    2
			pushi    8
			push    
			callk    Graph,  4
			pushi    2
			pushi    8
			lsl      cSaveBits
			callk    Graph,  4
			pushi    6
			pushi    12
			lsl      y1
			lsl      x1
			lsl      y2
			lsl      x2
			lsl      curMap
			callk    Graph,  12
code_047b:
			pushi    #modifyPath
			pushi    0
			self     4
			jmp      code_055b
code_0484:
			dup     
			ldi      15872
			eq?     
			bnt      code_0498
			pushi    1
			ldi      4
			sal      curMap
			push    
			callk    Show,  2
			jmp      code_055b
code_0498:
			dup     
			ldi      11776
			eq?     
			bnt      code_04ac
			pushi    1
			ldi      4
			sal      curMap
			push    
			callk    Show,  2
			jmp      code_055b
code_04ac:
			dup     
			ldi      15360
			eq?     
			bnt      code_04c0
			pushi    1
			ldi      1
			sal      curMap
			push    
			callk    Show,  2
			jmp      code_055b
code_04c0:
			dup     
			ldi      12032
			eq?     
			bnt      code_04d4
			pushi    1
			ldi      1
			sal      curMap
			push    
			callk    Show,  2
			jmp      code_055b
code_04d4:
			dup     
			ldi      27
			eq?     
			bnt      code_04e7
			ldi      1
			sat      temp3
			ldi      0
			sal      local657
			jmp      code_055b
code_04e7:
			dup     
			ldi      112
			eq?     
			bnt      code_0504
			lal      local660
			bnt      code_04fc
			ldi      0
			sal      local660
			jmp      code_055b
code_04fc:
			ldi      1
			sal      local660
			jmp      code_055b
code_0504:
			dup     
			ldi      101
			eq?     
			bnt      code_0530
			pushi    #signal
			pushi    0
			lag      ego
			send     4
			push    
			ldi      128
			and     
			bnt      code_0525
			pushi    #show
			pushi    0
			lag      ego
			send     4
			jmp      code_055b
code_0525:
			pushi    #hide
			pushi    0
			lag      ego
			send     4
			jmp      code_055b
code_0530:
			dup     
			ldi      63
			eq?     
			bnt      code_054a
			pushi    4
			pushi    PATHER
			pushi    7
			pushi    33
			pushi    999
			calle    Print,  8
			jmp      code_055b
code_054a:
			dup     
			ldi      1
			eq?     
			bnt      code_055b
			pushi    2
			pushi    PATHER
			pushi    8
			calle    Print,  4
code_055b:
			toss    
code_055c:
			lal      local662
			bnt      code_05d2
			lsl      lastX
			ldi      65535
			ne?     
			bnt      code_05d2
			lat      temp3
			not     
			bnt      code_05d2
			pushi    6
			pushi    7
			lsl      y1
			lsl      x1
			lsl      y2
			lsl      x2
			pushi    1
			callk    Graph,  12
			sal      vSaveBits
			pushi    6
			pushi    7
			lsl      y1
			lsl      x1
			lsl      y2
			lsl      x2
			pushi    4
			callk    Graph,  12
			sal      cSaveBits
			pushi    8
			pushi    4
			pushi    #y
			pushi    0
			lag      ego
			send     4
			push    
			pushi    #x
			pushi    0
			lag      ego
			send     4
			push    
			lsl      lastY
			lsl      lastX
			pushi    0
			pushi    65535
			pushi    10
			callk    Graph,  16
			pushi    6
			pushi    12
			lsl      y1
			lsl      x1
			lsl      y2
			lsl      x2
			lsl      curMap
			callk    Graph,  12
code_05d2:
			pushi    #dispose
			pushi    0
			lat      temp0
			send     4
			lal      local660
			bnt      code_0126
			lal      local662
			bnt      code_0126
			pushi    #new
			pushi    2
			pushi    #x
			pushi    0
			lag      ego
			send     4
			push    
			pushi    #y
			pushi    0
			lag      ego
			send     4
			push    
			lofsa    pathDialog
			send     8
			jmp      code_0126
code_0600:
			lag      modelessDialog
			bnt      code_060a
			pushi    #dispose
			pushi    0
			send     4
code_060a:
			lal      local657
			bnt      code_0616
			pushi    #savePoints
			pushi    0
			self     4
code_0616:
			pushi    4
			pushi    #curPic
			pushi    0
			lag      curRoom
			send     4
			push    
			pushi    100
			pushi    1
			lsg      currentPalette
			callk    DrawPic,  8
			lsg      overlays
			ldi      65535
			ne?     
			bnt      code_063d
			pushi    4
			lsg      overlays
			pushi    100
			pushi    0
			lsg      currentPalette
			callk    DrawPic,  8
code_063d:
			pushi    #doit
			pushi    0
			lag      addToPics
			send     4
			pushi    #drawPath
			pushi    1
			pushi    0
			self     6
			lsl      pathType
			ldi      1
			eq?     
			bnt      code_065c
			pushi    #polyTest
			pushi    0
			self     4
			jmp      code_0662
code_065c:
			pushi    #freewayTest
			pushi    0
			self     4
code_0662:
			pushi    #setLoop
			pushi    1
			pushi    65535
			pushi    242
			pushi    1
			pushi    65535
			pushi    18
			pushi    1
			lsl      local658
			lag      ego
			send     18
			lal      local661
			bnt      code_0689
			pushi    #hide
			pushi    0
			lag      ego
			send     4
			jmp      code_0691
code_0689:
			pushi    #show
			pushi    0
			lag      ego
			send     4
code_0691:
			ret     
		)
	)
	
	(method (handleEvent)
	)
	
	(method (modifyPath &tmp newEvent temp1 temp2 temp3 temp4 temp5 temp6 temp7)
		(= temp7 (= temp3 0))
		(= temp5 [pathPts 0])
		(= temp6 [pathPts 1])
		(= curPathStart (= temp4 0))
		(= lastX -1)
		(theGame
			setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
		)
		(DrawPic (curRoom curPic?) 100 TRUE currentPalette)
		(if (!= overlays -1)
			(DrawPic overlays 100 FALSE currentPalette)
		)
		(addToPics doit:)
		(Animate (cast elements?) 0)
		(while (not temp3)
			(SetPort 0)
			(= newEvent (Event new:))
			(newEvent y: (- (newEvent y?) 10))
			(if
				(= local662
					(if
						(or
							(!= (ego x?) (newEvent x?))
							(!= (ego y?) (newEvent y?))
						)
					else
						(newEvent type?)
					)
				)
				(if modelessDialog (modelessDialog dispose:))
				(if temp7
					(ego posn: (newEvent x?) (newEvent y?))
					(Animate (cast elements?) 0)
					(= temp1 ((User alterEgo?) onControl:))
					(Display PATHER 3 dsCOORD 3 1 dsWIDTH 0 dsBACKGROUND 3)
					(= temp1 (>> temp1 $0001))
					(= temp2 1)
					(while (< temp2 16)
						(if (& temp1 $0001)
							(Display PATHER 4 dsWIDTH 6 dsBACKGROUND temp2)
						else
							(Display PATHER 4 dsWIDTH 6 dsBACKGROUND 0)
						)
						(= temp1 (>> temp1 $0001))
						(++ temp2)
					)
					(if vSaveBits
						(Graph GRestoreBits vSaveBits)
						(Graph GRestoreBits cSaveBits)
						(Graph
							GShowBits
							y1
							x1
							y2
							x2
							curMap
						)
						(= vSaveBits 0)
						(SetVars (ego y?) (ego x?) lastY lastX)
					)
				else
					(Display PATHER 9 dsCOORD 3 1 dsWIDTH 0 dsBACKGROUND 3)
				)
			)
			(switch (newEvent type?)
				(1
					(if temp7
						(= temp5
							(= [pathPts temp4] (| (ego x?) (& temp5 $c800)))
						)
						(= temp6
							(= [pathPts (+ temp4 1)] (| (ego y?) (& temp6 $8000)))
						)
						(if (!= lastX -1)
							(SetVars
								lastY
								lastX
								(& temp6 $03ff)
								(& temp5 $03ff)
							)
							(= vSaveBits
								(Graph GSaveBits y1 x1 y2 x2 1)
							)
							(= cSaveBits
								(Graph GSaveBits y1 x1 y2 x2 4)
							)
							(Graph
								GDrawLine
								lastY
								lastX
								(& temp6 $03ff)
								(& temp5 $03ff)
								0
								-1
								10
							)
							(Graph
								GShowBits
								y1
								x1
								y2
								x2
								curMap
							)
						)
						(= temp7 0)
					)
				)
				(4
					(switch (newEvent message?)
						(27
							(= temp3 1)
							(ego posn: (newEvent x?) (newEvent y?))
							(Animate (cast elements?) 0)
						)
						(32
							(if (not temp7)
								(cond 
									((== temp4 (- index 2))
										(= temp3 1)
										(ego posn: (newEvent x?) (newEvent y?))
										(Animate (cast elements?) 0)
									)
									(
									(not (& [pathPts (= temp4 (+ temp4 2))] $c800))
										(if (!= lastX -1)
											(if vSaveBits
												(Graph GRestoreBits vSaveBits)
												(Graph GRestoreBits cSaveBits)
												(= vSaveBits 0)
											)
											(Graph
												GDrawLine
												lastY
												lastX
												(& temp6 $03ff)
												(& temp5 $03ff)
												0
												-1
												10
											)
											(Graph
												GShowBits
												y1
												x1
												y2
												x2
												curMap
											)
										)
										(if
										(and (& [pathPts (+ temp4 1)] $8000) (== pathType 1))
											(= [pathPts temp4] (& [pathPts curPathStart] $03ff))
											(= [pathPts (+ temp4 1)]
												(| [pathPts (+ curPathStart 1)] $8000)
											)
											(if (== temp4 (- index 2))
												(= temp3 1)
												(ego posn: (newEvent x?) (newEvent y?))
												(Animate (cast elements?) 0)
											else
												(= lastX -1)
												(= vSaveBits 0)
												(= curPathStart 9999)
											)
										else
											(= lastX (& temp5 $03ff))
											(= lastY (& temp6 $03ff))
										)
										(SetVars
											(& temp6 $03ff)
											(& temp5 $03ff)
											(& [pathPts (+ temp4 1)] $03ff)
											(& [pathPts temp4] $03ff)
										)
										(= vSaveBits
											(Graph GSaveBits y1 x1 y2 x2 1)
										)
										(= cSaveBits
											(Graph GSaveBits y1 x1 y2 x2 4)
										)
										(Graph
											GDrawLine
											(& temp6 $03ff)
											(& temp5 $03ff)
											(& [pathPts (+ temp4 1)] $03ff)
											(& [pathPts temp4] $03ff)
											0
											-1
											10
										)
										(Graph
											GShowBits
											y1
											x1
											y2
											x2
											curMap
										)
										(if (== curPathStart 9999) (= curPathStart (= temp4 (+ temp4 2))))
									)
									(else (= lastX -1) (= vSaveBits 0) (= curPathStart temp4))
								)
								(= temp5 [pathPts temp4])
								(= temp6 [pathPts (+ temp4 1)])
								(theGame
									setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
								)
							)
						)
						(13
							(= temp7 1)
							(if (!= lastX -1)
								(Graph GRestoreBits vSaveBits)
								(Graph GRestoreBits cSaveBits)
								(Graph
									GShowBits
									y1
									x1
									y2
									x2
									curMap
								)
								(ego posn: (newEvent x?) (newEvent y?))
								(Animate (cast elements?) 0)
								(SetVars (ego y?) (ego x?) lastY lastX)
							)
						)
						(99
							(if vSaveBits
								(Graph GRestoreBits vSaveBits)
								(Graph GRestoreBits cSaveBits)
								(Graph
									GShowBits
									y1
									x1
									y2
									x2
									curMap
								)
							)
							(if (& [pathPts temp4] $c800)
								(if (and vSaveBits (> temp4 4))
									(Graph
										GDrawLine
										(& [pathPts (- temp4 3)] $03ff)
										(& [pathPts (- temp4 4)] $03ff)
										(& [pathPts (- temp4 1)] $03ff)
										(& [pathPts (- temp4 2)] $03ff)
										0
										-1
										10
									)
									(Graph
										GShowBits
										y1
										x1
										y2
										x2
										curMap
									)
								)
								(= [pathPts (+ temp4 2)]
									(| [pathPts (+ temp4 2)] $c800)
								)
							)
							(= vSaveBits 0)
							(if (& [pathPts (+ temp4 1)] $8000)
								(= [pathPts (- temp4 1)]
									(| [pathPts (- temp4 1)] $8000)
								)
								(= curPathStart temp4)
							)
							(= index (- index 2))
							(= temp2 temp4)
							(while (< temp2 index)
								(= [pathPts temp2] [pathPts (+ temp2 2)])
								(= [pathPts (+ temp2 1)] [pathPts (+ temp2 3)])
								(= temp2 (+ temp2 2))
							)
							(if (& [pathPts temp4] $c800) (= lastX -1))
							(= temp5 [pathPts temp4])
							(= temp6 [pathPts (+ temp4 1)])
							(theGame
								setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
							)
							(if (!= lastX -1)
								(SetVars
									lastY
									lastX
									(& temp6 $03ff)
									(& temp5 $03ff)
								)
								(= vSaveBits
									(Graph GSaveBits y1 x1 y2 x2 1)
								)
								(= cSaveBits
									(Graph GSaveBits y1 x1 y2 x2 4)
								)
								(Graph
									GDrawLine
									lastY
									lastX
									(& temp6 $03ff)
									(& temp5 $03ff)
									0
									-1
									10
								)
								(Graph
									GShowBits
									y1
									x1
									y2
									x2
									curMap
								)
							)
							(cond 
								((== temp4 (- index 2)) (= temp3 1))
								(
									(and
										(& [pathPts (+ temp4 1)] $8000)
										(== pathType 1)
										(not (& [pathPts temp4] $c800))
									)
									(= curPathStart (= temp4 (+ temp4 2)))
									(= lastX -1)
									(= temp5 [pathPts temp4])
									(= temp6 [pathPts (+ temp4 1)])
									(theGame
										setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
									)
								)
							)
						)
						(105
							(if (!= lastX -1)
								(if vSaveBits
									(Graph GRestoreBits vSaveBits)
									(Graph GRestoreBits cSaveBits)
									(= vSaveBits 0)
								)
								(Graph
									GDrawLine
									lastY
									lastX
									(& temp6 $03ff)
									(& temp5 $03ff)
									0
									-1
									10
								)
								(Graph
									GShowBits
									y1
									x1
									y2
									x2
									curMap
								)
							)
							(= temp7 1)
							(= temp4 (+ temp4 2))
							(= temp2 index)
							(while (> temp2 temp4)
								(= [pathPts temp2] [pathPts (- temp2 2)])
								(= [pathPts (+ temp2 1)] [pathPts (- temp2 1)])
								(= temp2 (- temp2 2))
							)
							(= index (+ index 2))
							(if (& [pathPts (- temp4 1)] $8000)
								(= [pathPts temp4] [pathPts (- temp4 2)])
								(= [pathPts (+ temp4 1)] [pathPts (- temp4 1)])
								(= [pathPts (- temp4 1)]
									(& [pathPts (- temp4 1)] $03ff)
								)
							else
								(= [pathPts temp4] (& [pathPts temp4] $03ff))
								(= [pathPts (+ temp4 1)]
									(& [pathPts (+ temp4 1)] $03ff)
								)
							)
							(= lastX (& temp5 $03ff))
							(= lastY (& temp6 $03ff))
							(= temp5 [pathPts temp4])
							(= temp6 [pathPts (+ temp4 1)])
							(ego posn: (newEvent x?) (newEvent y?))
							(Animate (cast elements?) 0)
							(SetVars (ego y?) (ego x?) lastY lastX)
						)
						(15872 (Show (= curMap 4)))
						(11776 (Show (= curMap 4)))
						(15360 (Show (= curMap 1)))
						(12032 (Show (= curMap 1)))
						(112
							(if local660 (= local660 0) else (= local660 1))
						)
						(101
							(if (& (ego signal?) $0080)
								(ego show:)
							else
								(ego hide:)
							)
						)
						(63 (Print PATHER 10 #font 999))
						(1 (Print PATHER 8))
					)
				)
			)
			(if (and temp7 local662)
				(if (!= lastX -1)
					(= vSaveBits
						(Graph GSaveBits y1 x1 y2 x2 1)
					)
					(= cSaveBits
						(Graph GSaveBits y1 x1 y2 x2 4)
					)
					(Graph
						GDrawLine
						(ego y?)
						(ego x?)
						lastY
						lastX
						0
						-1
						10
					)
					(Graph
						GShowBits
						y1
						x1
						y2
						x2
						curMap
					)
				)
				(if local660 (pathDialog new: (ego x?) (ego y?)))
			)
			(newEvent dispose:)
		)
		(if vSaveBits
			(Graph GRestoreBits vSaveBits)
			(Graph GRestoreBits cSaveBits)
			(Graph
				GShowBits
				y1
				x1
				y2
				x2
				curMap
			)
			(= vSaveBits 0)
		)
		(self drawPath: 1)
		(if (& [pathPts (- index 1)] $8000)
			(= lastX -1)
		else
			(= lastX (& [pathPts (- index 2)] $03ff))
			(= lastY (& [pathPts (- index 1)] $03ff))
			(SetVars (ego y?) (ego x?) lastY lastX)
		)
	)
	
	(method (savePoints &tmp temp0 temp1 temp2 temp3)
		(LogIt
			(Format @local573 PATHER 11 (curRoom picture?))
		)
		(if (== pathType 2)
			(= temp1 0)
			(Print PATHER 12 #dispose)
			(LogIt (Format @local573 PATHER 13))
			(= temp0 1)
			(while (< temp0 index)
				(if (& [pathPts temp0] $8000) (++ temp1))
				(= temp0 (+ temp0 2))
			)
			(LogIt (Format @local573 PATHER 14 temp1))
			(= temp0 0)
			(while (< temp0 temp1)
				(LogIt {0_})
				(++ temp0)
			)
			(LogIt (Format @local573 PATHER 15))
			(= temp1 0)
			(= temp0 0)
			(while (< temp0 index)
				(cond 
					((& [pathPts temp0] $c800)
						(LogIt
							(Format
								@local573
								PATHER
								16
								(& [pathPts temp0] $03ff)
								[pathPts (+ temp0 1)]
								temp1
							)
						)
					)
					((& [pathPts (+ temp0 1)] $8000)
						(LogIt
							(Format
								@local573
								PATHER
								17
								[pathPts temp0]
								(& [pathPts (+ temp0 1)] $03ff)
							)
						)
						(++ temp1)
					)
					(else
						(LogIt
							(Format
								@local573
								PATHER
								18
								[pathPts temp0]
								[pathPts (+ temp0 1)]
							)
						)
					)
				)
				(= temp0 (+ temp0 2))
			)
			(LogIt (Format @local573 PATHER 19))
		else
			(Print PATHER 20 #dispose)
			(= temp1 1)
			(= temp3 0)
			(= temp0 0)
			(while (< temp0 index)
				(cond 
					((& [pathPts temp0] $c800)
						(LogIt
							(Format
								@local573
								PATHER
								21
								temp1
								(& [pathPts temp0] $03ff)
								[pathPts (+ temp0 1)]
							)
						)
						(= temp3 0)
					)
					((& [pathPts (+ temp0 1)] $8000) (LogIt (Format @local573 PATHER 22)) (++ temp1))
					((< temp3 10)
						(LogIt
							(Format
								@local573
								PATHER
								23
								[pathPts temp0]
								[pathPts (+ temp0 1)]
							)
						)
						(++ temp3)
					)
					(else
						(LogIt
							(Format
								@local573
								PATHER
								24
								[pathPts temp0]
								[pathPts (+ temp0 1)]
							)
						)
						(= temp3 0)
					)
				)
				(= temp0 (+ temp0 2))
			)
			(LogIt (Format @local573 PATHER 25))
			(= temp1 1)
			(= temp2 1)
			(= temp0 0)
			(while (< temp0 index)
				(if (& [pathPts (+ temp0 1)] $8000)
					(LogIt
						(Format @local573 PATHER 26 temp1 temp1 (- temp2 1))
					)
					(++ temp1)
					(= temp2 1)
				else
					(++ temp2)
				)
				(= temp0 (+ temp0 2))
			)
			(LogIt (Format @local573 PATHER 27))
			(= temp0 1)
			(while (< temp0 temp1)
				(LogIt (Format @local573 PATHER 28 temp0))
				(++ temp0)
			)
			(LogIt (Format @local573 PATHER 29))
			(= temp0 1)
			(while (< temp0 temp1)
				(LogIt
					(Format @local573 PATHER 30 temp0 [local663 [pathHdr temp0]])
				)
				(++ temp0)
			)
		)
		(if modelessDialog (modelessDialog dispose:))
	)
	
	(method (getFreeway &tmp [temp0 4])
	)
	
	(method (getPoly)
		(if (curRoom obstacles?)
			(= local659 0)
			((curRoom obstacles?)
				eachElementDo: #perform addSelfToPath
			)
		)
		(self drawPath: 1)
	)
	
	(method (drawPath param1 &tmp temp0)
		(if (and (!= 0 param1) (!= param1 1)) (= param1 1))
		(self fixPoints:)
		(= curPathStart (= temp0 0))
		(while (< temp0 index)
			(if (not (& [pathPts temp0] $c800))
				(if (> temp0 1)
					(if
					(and (& [pathPts (+ temp0 1)] $8000) (== pathType 1))
						(= [pathPts temp0] (& [pathPts curPathStart] $03ff))
						(= [pathPts (+ temp0 1)]
							(| [pathPts (+ curPathStart 1)] $8000)
						)
					)
					(SetVars
						(& [pathPts (- temp0 1)] $03ff)
						(& [pathPts (- temp0 2)] $03ff)
						(& [pathPts (+ temp0 1)] $03ff)
						(& [pathPts temp0] $03ff)
					)
					(Graph
						GDrawLine
						(& [pathPts (- temp0 1)] $03ff)
						(& [pathPts (- temp0 2)] $03ff)
						(& [pathPts (+ temp0 1)] $03ff)
						(& [pathPts temp0] $03ff)
						0
						-1
						(- (* 11 param1) 1)
					)
					(Graph
						GShowBits
						y1
						x1
						y2
						x2
						curMap
					)
				)
				(if (& [pathPts (+ temp0 1)] $8000)
					(= curPathStart (+ temp0 2))
				)
			)
			(= temp0 (+ temp0 2))
		)
	)
	
	(method (fixPoints &tmp temp0)
		(= temp0 0)
		(while (< temp0 index)
			(cond 
				((>= (& [pathPts temp0] $03ff) 640) (= [pathPts temp0] (| $0000 (& [pathPts temp0] $c800))))
				((>= (& [pathPts temp0] $03ff) 320) (= [pathPts temp0] (| $013f (& [pathPts temp0] $c800))))
			)
			(cond 
				((>= (& [pathPts (+ temp0 1)] $03ff) 400)
					(= [pathPts (+ temp0 1)]
						(| $0000 (& [pathPts (+ temp0 1)] $8000))
					)
				)
				((>= (& [pathPts (+ temp0 1)] $03ff) 190)
					(= [pathPts (+ temp0 1)]
						(| $00bd (& [pathPts (+ temp0 1)] $8000))
					)
				)
			)
			(= temp0 (+ temp0 2))
		)
	)
	
	(method (polyTest &tmp temp0 temp1)
		(if (curRoom obstacles?)
			((curRoom obstacles?) eachElementDo: #dispose release:)
		)
		(= curPathStart (= temp0 0))
		(= local659 0)
		(= temp1 0)
		(while (< temp1 index)
			(if temp0
				(if (& [pathPts (+ temp1 1)] $8000)
					(= [pathPts temp1] (& [pathPts temp1] $03ff))
					(= [pathPts (+ temp1 1)]
						(& [pathPts (+ temp1 1)] $03ff)
					)
					(curRoom
						addObstacle:
							((Clone Polygon)
								points: @[pathPts curPathStart]
								size: (- (/ (+ (- temp1 curPathStart) 2) 2) 1)
								type: [pathHdr (++ local659)]
								yourself:
							)
					)
					(= temp0 0)
				)
			else
				(if (& [pathPts temp1] $c800)
					(= curPathStart temp1)
					(= temp0 1)
				)
				(= [pathPts temp1] (& [pathPts temp1] $03ff))
				(= [pathPts (+ temp1 1)]
					(& [pathPts (+ temp1 1)] $03ff)
				)
			)
			(= temp1 (+ temp1 2))
		)
	)
	
	(method (freewayTest &tmp [temp0 2])
	)
	
	(method (dispose)
		(features delete: self)
		(DisposeScript PATHER)
	)
)

(instance addSelfToPath of Code
	(properties)
	
	(method (doit thePoly &tmp temp0)
		(if (== (thePoly superClass?) Polygon)
			(StrCpy @[pathPts index] (thePoly points?) (- (* (thePoly size?) 4)))
			(= [pathPts index] (| [pathPts index] $c800))
			(= temp0 (+ index 2))
			(while (< temp0 (+ index (* (thePoly size?) 2)))
				(= [pathPts temp0] (& [pathPts temp0] $03ff))
				(= [pathPts (+ temp0 1)]
					(& [pathPts (+ temp0 1)] $03ff)
				)
				(= temp0 (+ temp0 2))
			)
			(= index (+ index (* (thePoly size?) 2)))
			(StrCpy @[pathPts index] (thePoly points?) -4)
			(= curPathStart (= index (+ index 2)))
			(= [pathPts (- index 2)]
				(& [pathPts (- index 2)] $03ff)
			)
			(= [pathPts (- index 1)]
				(| [pathPts (- index 1)] $8000)
			)
			(= [pathHdr (++ local659)] (thePoly type?))
		)
	)
)

(instance pathDialog of Dialog
	(properties)
	
	(method (new param1 param2 &tmp newSuper newDText temp2 [temp3 100])
		((= newSuper (super new:))
			window: SysWindow
			name: {X/Y}
		)
		(= newDText (DText new:))
		(Format @temp3 PATHER 31 param1 param2)
		(newDText text: @temp3 moveTo: 0 0 font: 999 setSize:)
		(newSuper
			add: newDText
			setSize:
			moveTo:
				(if (== -1 param1) (newSuper nsLeft?) else param1)
				(if (== -1 param2) (newSuper nsTop?) else (- param2 8))
		)
		(= temp2 (GetPort))
		(newSuper open: 0 15)
		(= modelessPort (GetPort))
		(SetPort temp2)
		(= modelessDialog newSuper)
	)
	
	(method (setSize)
		(super setSize: &rest)
		(= nsRight (- nsRight 4))
		(= nsBottom (- nsBottom 4))
	)
)
