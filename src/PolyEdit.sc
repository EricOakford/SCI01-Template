;;; Sierra Script 1.0 - (do not remove this comment)
(script# 943)
(include sci.sh)
(use Main)
(use Intrface)
(use Polygon)
(use Save)
(use File)
(use User)
(use System)


(local
	local0
	local1
	local2
	local3
	local4
	local5 =  -1
	local6
	local7
	[local8 15]
	[local23 50]
	[local73 500]
	[local573 80]
	local653
	local654
	local655
	local656
	local657
	local658
	local659
	local660
	local661
	local662
	[local663 3] = [{PTotalAccess} {PNearestAccess} {PBarredAccess}]
)
(procedure (localproc_181e param1 param2 param3 param4)
	(if (> param1 param3)
		(= local654 param3)
		(= local656 param1)
	else
		(= local654 param1)
		(= local656 param3)
	)
	(if (> param2 param4)
		(= local653 param4)
		(= local655 param2)
	else
		(= local653 param2)
		(= local655 param4)
	)
	(-- local653)
	(-- local654)
	(++ local655)
	(return (++ local656))
)

(procedure (localproc_1869 param1)
	(File name: @local8 writeString: param1 close:)
	(DisposeScript 993)
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
			pushi    943
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
			sal      local0
			not     
			bnt      code_0056
			ret     
code_0056:
			pushi    4
			lea      @local8
			push    
			pushi    943
			pushi    1
			pushi    #picture
			pushi    0
			lag      curRoom
			send     4
			push    
			callk    Format,  8
			pushi    3
			lea      @local8
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
			sal      local7
			push    
			callk    Show,  2
			ldi      0
			sal      local3
			sal      local4
			pushi    #obstacles
			pushi    0
			lag      curRoom
			send     4
			bnt      code_0104
			lsl      local0
			ldi      1
			eq?     
			bnt      code_0104
			pushi    10
			pushi    943
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
			lsl      local0
			ldi      2
			eq?     
			bnt      code_00fe
			pushi    #oldMover
			pushi    0
			self     4
			jmp      code_0104
code_00fe:
			pushi    #loopIndex
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
			lal      local3
			sali     local73
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
			pushi    943
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
			pushi    943
			pushi    4
			pushi    106
			pushi    6
			pushi    103
			lst      temp2
			callk    Display,  12
			jmp      code_021a
code_0209:
			pushi    6
			pushi    943
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
			lal      local1
			bnt      code_0259
			pushi    2
			pushi    8
			push    
			callk    Graph,  4
			pushi    2
			pushi    8
			lsl      local2
			callk    Graph,  4
			pushi    6
			pushi    12
			lsl      local654
			lsl      local653
			lsl      local656
			lsl      local655
			lsl      local7
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
			lsl      local6
			lsl      local5
			call     localproc_181e,  8
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
			lsl      local3
			lal      local4
			gt?     
			bnt      code_039e
			lsl      local0
			ldi      1
			eq?     
			bnt      code_037c
			lal      local4
			lsli     local73
			ldi      1023
			and     
			push    
			lal      local3
			sali     local73
			lsl      local4
			ldi      1
			add     
			lsli     local73
			ldi      32768
			or      
			push    
			+al      local3
			sali     local73
			pushi    8
			pushi    4
			lsl      local3
			ldi      2
			sub     
			lsli     local73
			ldi      1023
			and     
			push    
			lsl      local3
			ldi      3
			sub     
			lsli     local73
			ldi      1023
			and     
			push    
			lal      local3
			lsli     local73
			ldi      1023
			and     
			push    
			lsl      local3
			ldi      1
			sub     
			lsli     local73
			ldi      1023
			and     
			push    
			pushi    0
			pushi    65535
			pushi    10
			callk    Graph,  16
			pushi    4
			lsl      local3
			ldi      2
			sub     
			lsli     local73
			ldi      1023
			and     
			push    
			lsl      local3
			ldi      3
			sub     
			lsli     local73
			ldi      1023
			and     
			push    
			lal      local3
			lsli     local73
			ldi      1023
			and     
			push    
			lsl      local3
			ldi      1
			sub     
			lsli     local73
			ldi      1023
			and     
			push    
			call     localproc_181e,  8
			pushi    3
			lea      @local573
			push    
			pushi    943
			pushi    5
			callk    Format,  6
			pushi    6
			pushi    12
			lsl      local654
			lsl      local653
			lsl      local656
			lsl      local655
			lsl      local7
			callk    Graph,  12
			pushi    13
			pushi    943
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
			sali     local23
			jmp      code_0389
code_037c:
			-al      local3
			lsli     local73
			ldi      32768
			or      
			push    
			lal      local3
			sali     local73
code_0389:
			pushi    65535
			+al      local3
			sali     local73
			lal      local3
			sal      local4
			ldi      65535
			sal      local5
			ldi      0
			sal      local1
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
			lsl      local5
			ldi      65535
			eq?     
			bnt      code_03ca
			pushi    #x
			pushi    0
			lag      ego
			send     4
			sal      local5
			push    
			ldi      51200
			or      
			push    
			lal      local3
			sali     local73
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
			lsl      local6
			lsl      local5
			pushi    0
			pushi    65535
			pushi    10
			callk    Graph,  16
			pushi    6
			pushi    12
			lsl      local654
			lsl      local653
			lsl      local656
			lsl      local655
			lsl      local7
			callk    Graph,  12
			pushi    #x
			pushi    0
			lag      ego
			send     4
			sal      local5
			push    
			lal      local3
			sali     local73
code_040d:
			pushi    #y
			pushi    0
			lag      ego
			send     4
			sal      local6
			push    
			+al      local3
			sali     local73
			+al      local3
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
			lal      local3
			sali     local73
			jmp      code_055b
code_044a:
			dup     
			ldi      12800
			eq?     
			bnt      code_0484
			lal      local1
			bnt      code_047b
			pushi    2
			pushi    8
			push    
			callk    Graph,  4
			pushi    2
			pushi    8
			lsl      local2
			callk    Graph,  4
			pushi    6
			pushi    12
			lsl      local654
			lsl      local653
			lsl      local656
			lsl      local655
			lsl      local7
			callk    Graph,  12
code_047b:
			pushi    #shiftParser
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
			sal      local7
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
			sal      local7
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
			sal      local7
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
			sal      local7
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
			pushi    943
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
			pushi    943
			pushi    8
			calle    Print,  4
code_055b:
			toss    
code_055c:
			lal      local662
			bnt      code_05d2
			lsl      local5
			ldi      65535
			ne?     
			bnt      code_05d2
			lat      temp3
			not     
			bnt      code_05d2
			pushi    6
			pushi    7
			lsl      local654
			lsl      local653
			lsl      local656
			lsl      local655
			pushi    1
			callk    Graph,  12
			sal      local1
			pushi    6
			pushi    7
			lsl      local654
			lsl      local653
			lsl      local656
			lsl      local655
			pushi    4
			callk    Graph,  12
			sal      local2
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
			lsl      local6
			lsl      local5
			pushi    0
			pushi    65535
			pushi    10
			callk    Graph,  16
			pushi    6
			pushi    12
			lsl      local654
			lsl      local653
			lsl      local656
			lsl      local655
			lsl      local7
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
			pushi    #oldCycler
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
			pushi    #numOfLoops
			pushi    1
			pushi    0
			self     6
			lsl      local0
			ldi      1
			eq?     
			bnt      code_065c
			pushi    #nextLoop
			pushi    0
			self     4
			jmp      code_0662
code_065c:
			pushi    #newMover
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
	
	(method (shiftParser &tmp newEvent temp1 temp2 temp3 temp4 temp5 temp6 temp7)
		(= temp7 (= temp3 0))
		(= temp5 [local73 0])
		(= temp6 [local73 1])
		(= local4 (= temp4 0))
		(= local5 -1)
		(theGame
			setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
		)
		(DrawPic (curRoom curPic?) 100 dpCLEAR currentPalette)
		(if (!= overlays -1)
			(DrawPic overlays 100 dpNO_CLEAR currentPalette)
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
					(Display 943 3 100 3 1 106 0 103 3)
					(= temp1 (>> temp1 $0001))
					(= temp2 1)
					(while (< temp2 16)
						(if (& temp1 $0001)
							(Display 943 4 106 6 103 temp2)
						else
							(Display 943 4 106 6 103 0)
						)
						(= temp1 (>> temp1 $0001))
						(++ temp2)
					)
					(if local1
						(Graph grRESTORE_BOX local1)
						(Graph grRESTORE_BOX local2)
						(Graph
							grUPDATE_BOX
							local654
							local653
							local656
							local655
							local7
						)
						(= local1 0)
						(localproc_181e (ego y?) (ego x?) local6 local5)
					)
				else
					(Display 943 9 100 3 1 106 0 103 3)
				)
			)
			(switch (newEvent type?)
				(1
					(if temp7
						(= temp5
							(= [local73 temp4] (| (ego x?) (& temp5 $c800)))
						)
						(= temp6
							(= [local73 (+ temp4 1)] (| (ego y?) (& temp6 $8000)))
						)
						(if (!= local5 -1)
							(localproc_181e
								local6
								local5
								(& temp6 $03ff)
								(& temp5 $03ff)
							)
							(= local1
								(Graph grSAVE_BOX local654 local653 local656 local655 1)
							)
							(= local2
								(Graph grSAVE_BOX local654 local653 local656 local655 4)
							)
							(Graph
								grDRAW_LINE
								local6
								local5
								(& temp6 $03ff)
								(& temp5 $03ff)
								0
								-1
								10
							)
							(Graph
								grUPDATE_BOX
								local654
								local653
								local656
								local655
								local7
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
									((== temp4 (- local3 2))
										(= temp3 1)
										(ego posn: (newEvent x?) (newEvent y?))
										(Animate (cast elements?) 0)
									)
									(
									(not (& [local73 (= temp4 (+ temp4 2))] $c800))
										(if (!= local5 -1)
											(if local1
												(Graph grRESTORE_BOX local1)
												(Graph grRESTORE_BOX local2)
												(= local1 0)
											)
											(Graph
												grDRAW_LINE
												local6
												local5
												(& temp6 $03ff)
												(& temp5 $03ff)
												0
												-1
												10
											)
											(Graph
												grUPDATE_BOX
												local654
												local653
												local656
												local655
												local7
											)
										)
										(if
										(and (& [local73 (+ temp4 1)] $8000) (== local0 1))
											(= [local73 temp4] (& [local73 local4] $03ff))
											(= [local73 (+ temp4 1)]
												(| [local73 (+ local4 1)] $8000)
											)
											(if (== temp4 (- local3 2))
												(= temp3 1)
												(ego posn: (newEvent x?) (newEvent y?))
												(Animate (cast elements?) 0)
											else
												(= local5 -1)
												(= local1 0)
												(= local4 9999)
											)
										else
											(= local5 (& temp5 $03ff))
											(= local6 (& temp6 $03ff))
										)
										(localproc_181e
											(& temp6 $03ff)
											(& temp5 $03ff)
											(& [local73 (+ temp4 1)] $03ff)
											(& [local73 temp4] $03ff)
										)
										(= local1
											(Graph grSAVE_BOX local654 local653 local656 local655 1)
										)
										(= local2
											(Graph grSAVE_BOX local654 local653 local656 local655 4)
										)
										(Graph
											grDRAW_LINE
											(& temp6 $03ff)
											(& temp5 $03ff)
											(& [local73 (+ temp4 1)] $03ff)
											(& [local73 temp4] $03ff)
											0
											-1
											10
										)
										(Graph
											grUPDATE_BOX
											local654
											local653
											local656
											local655
											local7
										)
										(if (== local4 9999) (= local4 (= temp4 (+ temp4 2))))
									)
									(else (= local5 -1) (= local1 0) (= local4 temp4))
								)
								(= temp5 [local73 temp4])
								(= temp6 [local73 (+ temp4 1)])
								(theGame
									setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
								)
							)
						)
						(13
							(= temp7 1)
							(if (!= local5 -1)
								(Graph grRESTORE_BOX local1)
								(Graph grRESTORE_BOX local2)
								(Graph
									grUPDATE_BOX
									local654
									local653
									local656
									local655
									local7
								)
								(ego posn: (newEvent x?) (newEvent y?))
								(Animate (cast elements?) 0)
								(localproc_181e (ego y?) (ego x?) local6 local5)
							)
						)
						(99
							(if local1
								(Graph grRESTORE_BOX local1)
								(Graph grRESTORE_BOX local2)
								(Graph
									grUPDATE_BOX
									local654
									local653
									local656
									local655
									local7
								)
							)
							(if (& [local73 temp4] $c800)
								(if (and local1 (> temp4 4))
									(Graph
										grDRAW_LINE
										(& [local73 (- temp4 3)] $03ff)
										(& [local73 (- temp4 4)] $03ff)
										(& [local73 (- temp4 1)] $03ff)
										(& [local73 (- temp4 2)] $03ff)
										0
										-1
										10
									)
									(Graph
										grUPDATE_BOX
										local654
										local653
										local656
										local655
										local7
									)
								)
								(= [local73 (+ temp4 2)]
									(| [local73 (+ temp4 2)] $c800)
								)
							)
							(= local1 0)
							(if (& [local73 (+ temp4 1)] $8000)
								(= [local73 (- temp4 1)]
									(| [local73 (- temp4 1)] $8000)
								)
								(= local4 temp4)
							)
							(= local3 (- local3 2))
							(= temp2 temp4)
							(while (< temp2 local3)
								(= [local73 temp2] [local73 (+ temp2 2)])
								(= [local73 (+ temp2 1)] [local73 (+ temp2 3)])
								(= temp2 (+ temp2 2))
							)
							(if (& [local73 temp4] $c800) (= local5 -1))
							(= temp5 [local73 temp4])
							(= temp6 [local73 (+ temp4 1)])
							(theGame
								setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
							)
							(if (!= local5 -1)
								(localproc_181e
									local6
									local5
									(& temp6 $03ff)
									(& temp5 $03ff)
								)
								(= local1
									(Graph grSAVE_BOX local654 local653 local656 local655 1)
								)
								(= local2
									(Graph grSAVE_BOX local654 local653 local656 local655 4)
								)
								(Graph
									grDRAW_LINE
									local6
									local5
									(& temp6 $03ff)
									(& temp5 $03ff)
									0
									-1
									10
								)
								(Graph
									grUPDATE_BOX
									local654
									local653
									local656
									local655
									local7
								)
							)
							(cond 
								((== temp4 (- local3 2)) (= temp3 1))
								(
									(and
										(& [local73 (+ temp4 1)] $8000)
										(== local0 1)
										(not (& [local73 temp4] $c800))
									)
									(= local4 (= temp4 (+ temp4 2)))
									(= local5 -1)
									(= temp5 [local73 temp4])
									(= temp6 [local73 (+ temp4 1)])
									(theGame
										setCursor: normalCursor (HaveMouse) (& temp5 $03ff) (& temp6 $03ff)
									)
								)
							)
						)
						(105
							(if (!= local5 -1)
								(if local1
									(Graph grRESTORE_BOX local1)
									(Graph grRESTORE_BOX local2)
									(= local1 0)
								)
								(Graph
									grDRAW_LINE
									local6
									local5
									(& temp6 $03ff)
									(& temp5 $03ff)
									0
									-1
									10
								)
								(Graph
									grUPDATE_BOX
									local654
									local653
									local656
									local655
									local7
								)
							)
							(= temp7 1)
							(= temp4 (+ temp4 2))
							(= temp2 local3)
							(while (> temp2 temp4)
								(= [local73 temp2] [local73 (- temp2 2)])
								(= [local73 (+ temp2 1)] [local73 (- temp2 1)])
								(= temp2 (- temp2 2))
							)
							(= local3 (+ local3 2))
							(if (& [local73 (- temp4 1)] $8000)
								(= [local73 temp4] [local73 (- temp4 2)])
								(= [local73 (+ temp4 1)] [local73 (- temp4 1)])
								(= [local73 (- temp4 1)]
									(& [local73 (- temp4 1)] $03ff)
								)
							else
								(= [local73 temp4] (& [local73 temp4] $03ff))
								(= [local73 (+ temp4 1)]
									(& [local73 (+ temp4 1)] $03ff)
								)
							)
							(= local5 (& temp5 $03ff))
							(= local6 (& temp6 $03ff))
							(= temp5 [local73 temp4])
							(= temp6 [local73 (+ temp4 1)])
							(ego posn: (newEvent x?) (newEvent y?))
							(Animate (cast elements?) 0)
							(localproc_181e (ego y?) (ego x?) local6 local5)
						)
						(15872 (Show (= local7 4)))
						(11776 (Show (= local7 4)))
						(15360 (Show (= local7 1)))
						(12032 (Show (= local7 1)))
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
						(63 (Print 943 10 33 999))
						(1 (Print 943 8))
					)
				)
			)
			(if (and temp7 local662)
				(if (!= local5 -1)
					(= local1
						(Graph grSAVE_BOX local654 local653 local656 local655 1)
					)
					(= local2
						(Graph grSAVE_BOX local654 local653 local656 local655 4)
					)
					(Graph
						grDRAW_LINE
						(ego y?)
						(ego x?)
						local6
						local5
						0
						-1
						10
					)
					(Graph
						grUPDATE_BOX
						local654
						local653
						local656
						local655
						local7
					)
				)
				(if local660 (pathDialog new: (ego x?) (ego y?)))
			)
			(newEvent dispose:)
		)
		(if local1
			(Graph grRESTORE_BOX local1)
			(Graph grRESTORE_BOX local2)
			(Graph
				grUPDATE_BOX
				local654
				local653
				local656
				local655
				local7
			)
			(= local1 0)
		)
		(self numOfLoops: 1)
		(if (& [local73 (- local3 1)] $8000)
			(= local5 -1)
		else
			(= local5 (& [local73 (- local3 2)] $03ff))
			(= local6 (& [local73 (- local3 1)] $03ff))
			(localproc_181e (ego y?) (ego x?) local6 local5)
		)
	)
	
	(method (oldCycler &tmp temp0 temp1 temp2 temp3)
		(localproc_1869
			(Format @local573 943 11 (curRoom picture?))
		)
		(if (== local0 2)
			(= temp1 0)
			(Print 943 12 103)
			(localproc_1869 (Format @local573 943 13))
			(= temp0 1)
			(while (< temp0 local3)
				(if (& [local73 temp0] $8000) (++ temp1))
				(= temp0 (+ temp0 2))
			)
			(localproc_1869 (Format @local573 943 14 temp1))
			(= temp0 0)
			(while (< temp0 temp1)
				(localproc_1869 {0_})
				(++ temp0)
			)
			(localproc_1869 (Format @local573 943 15))
			(= temp1 0)
			(= temp0 0)
			(while (< temp0 local3)
				(cond 
					((& [local73 temp0] $c800)
						(localproc_1869
							(Format
								@local573
								943
								16
								(& [local73 temp0] $03ff)
								[local73 (+ temp0 1)]
								temp1
							)
						)
					)
					((& [local73 (+ temp0 1)] $8000)
						(localproc_1869
							(Format
								@local573
								943
								17
								[local73 temp0]
								(& [local73 (+ temp0 1)] $03ff)
							)
						)
						(++ temp1)
					)
					(else
						(localproc_1869
							(Format
								@local573
								943
								18
								[local73 temp0]
								[local73 (+ temp0 1)]
							)
						)
					)
				)
				(= temp0 (+ temp0 2))
			)
			(localproc_1869 (Format @local573 943 19))
		else
			(Print 943 20 103)
			(= temp1 1)
			(= temp3 0)
			(= temp0 0)
			(while (< temp0 local3)
				(cond 
					((& [local73 temp0] $c800)
						(localproc_1869
							(Format
								@local573
								943
								21
								temp1
								(& [local73 temp0] $03ff)
								[local73 (+ temp0 1)]
							)
						)
						(= temp3 0)
					)
					((& [local73 (+ temp0 1)] $8000) (localproc_1869 (Format @local573 943 22)) (++ temp1))
					((< temp3 10)
						(localproc_1869
							(Format
								@local573
								943
								23
								[local73 temp0]
								[local73 (+ temp0 1)]
							)
						)
						(++ temp3)
					)
					(else
						(localproc_1869
							(Format
								@local573
								943
								24
								[local73 temp0]
								[local73 (+ temp0 1)]
							)
						)
						(= temp3 0)
					)
				)
				(= temp0 (+ temp0 2))
			)
			(localproc_1869 (Format @local573 943 25))
			(= temp1 1)
			(= temp2 1)
			(= temp0 0)
			(while (< temp0 local3)
				(if (& [local73 (+ temp0 1)] $8000)
					(localproc_1869
						(Format @local573 943 26 temp1 temp1 (- temp2 1))
					)
					(++ temp1)
					(= temp2 1)
				else
					(++ temp2)
				)
				(= temp0 (+ temp0 2))
			)
			(localproc_1869 (Format @local573 943 27))
			(= temp0 1)
			(while (< temp0 temp1)
				(localproc_1869 (Format @local573 943 28 temp0))
				(++ temp0)
			)
			(localproc_1869 (Format @local573 943 29))
			(= temp0 1)
			(while (< temp0 temp1)
				(localproc_1869
					(Format @local573 943 30 temp0 [local663 [local23 temp0]])
				)
				(++ temp0)
			)
		)
		(if modelessDialog (modelessDialog dispose:))
	)
	
	(method (oldMover &tmp [temp0 4])
	)
	
	(method (loopIndex)
		(if (curRoom obstacles?)
			(= local659 0)
			((curRoom obstacles?)
				eachElementDo: #perform addSelfToPath
			)
		)
		(self numOfLoops: 1)
	)
	
	(method (numOfLoops param1 &tmp temp0)
		(if (and (!= 0 param1) (!= param1 1)) (= param1 1))
		(self loopIsCorrect:)
		(= local4 (= temp0 0))
		(while (< temp0 local3)
			(if (not (& [local73 temp0] $c800))
				(if (> temp0 1)
					(if
					(and (& [local73 (+ temp0 1)] $8000) (== local0 1))
						(= [local73 temp0] (& [local73 local4] $03ff))
						(= [local73 (+ temp0 1)]
							(| [local73 (+ local4 1)] $8000)
						)
					)
					(localproc_181e
						(& [local73 (- temp0 1)] $03ff)
						(& [local73 (- temp0 2)] $03ff)
						(& [local73 (+ temp0 1)] $03ff)
						(& [local73 temp0] $03ff)
					)
					(Graph
						grDRAW_LINE
						(& [local73 (- temp0 1)] $03ff)
						(& [local73 (- temp0 2)] $03ff)
						(& [local73 (+ temp0 1)] $03ff)
						(& [local73 temp0] $03ff)
						0
						-1
						(- (* 11 param1) 1)
					)
					(Graph
						grUPDATE_BOX
						local654
						local653
						local656
						local655
						local7
					)
				)
				(if (& [local73 (+ temp0 1)] $8000)
					(= local4 (+ temp0 2))
				)
			)
			(= temp0 (+ temp0 2))
		)
	)
	
	(method (loopIsCorrect &tmp temp0)
		(= temp0 0)
		(while (< temp0 local3)
			(cond 
				((>= (& [local73 temp0] $03ff) 640) (= [local73 temp0] (| $0000 (& [local73 temp0] $c800))))
				((>= (& [local73 temp0] $03ff) 320) (= [local73 temp0] (| $013f (& [local73 temp0] $c800))))
			)
			(cond 
				((>= (& [local73 (+ temp0 1)] $03ff) 400)
					(= [local73 (+ temp0 1)]
						(| $0000 (& [local73 (+ temp0 1)] $8000))
					)
				)
				((>= (& [local73 (+ temp0 1)] $03ff) 190)
					(= [local73 (+ temp0 1)]
						(| $00bd (& [local73 (+ temp0 1)] $8000))
					)
				)
			)
			(= temp0 (+ temp0 2))
		)
	)
	
	(method (nextLoop &tmp temp0 temp1)
		(if (curRoom obstacles?)
			((curRoom obstacles?) eachElementDo: #dispose release:)
		)
		(= local4 (= temp0 0))
		(= local659 0)
		(= temp1 0)
		(while (< temp1 local3)
			(if temp0
				(if (& [local73 (+ temp1 1)] $8000)
					(= [local73 temp1] (& [local73 temp1] $03ff))
					(= [local73 (+ temp1 1)]
						(& [local73 (+ temp1 1)] $03ff)
					)
					(curRoom
						addObstacle:
							((Clone Polygon)
								points: @[local73 local4]
								size: (- (/ (+ (- temp1 local4) 2) 2) 1)
								type: [local23 (++ local659)]
								yourself:
							)
					)
					(= temp0 0)
				)
			else
				(if (& [local73 temp1] $c800)
					(= local4 temp1)
					(= temp0 1)
				)
				(= [local73 temp1] (& [local73 temp1] $03ff))
				(= [local73 (+ temp1 1)]
					(& [local73 (+ temp1 1)] $03ff)
				)
			)
			(= temp1 (+ temp1 2))
		)
	)
	
	(method (newMover &tmp [temp0 2])
	)
	
	(method (dispose)
		(features delete: self)
		(DisposeScript 943)
	)
)

(instance addSelfToPath of Code
	(properties)
	
	(method (doit param1 &tmp temp0)
		(if (== (param1 superClass?) Polygon)
			(StrCpy
				@[local73
				local3]
				(param1 points?)
				(- (* (param1 size?) 4))
			)
			(= [local73 local3] (| [local73 local3] $c800))
			(= temp0 (+ local3 2))
			(while (< temp0 (+ local3 (* (param1 size?) 2)))
				(= [local73 temp0] (& [local73 temp0] $03ff))
				(= [local73 (+ temp0 1)]
					(& [local73 (+ temp0 1)] $03ff)
				)
				(= temp0 (+ temp0 2))
			)
			(= local3 (+ local3 (* (param1 size?) 2)))
			(StrCpy @[local73 local3] (param1 points?) -4)
			(= local4 (= local3 (+ local3 2)))
			(= [local73 (- local3 2)]
				(& [local73 (- local3 2)] $03ff)
			)
			(= [local73 (- local3 1)]
				(| [local73 (- local3 1)] $8000)
			)
			(= [local23 (++ local659)] (param1 type?))
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
		(Format @temp3 943 31 param1 param2)
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
