;;; Sierra Script 1.0 - (do not remove this comment)
(script# 954)
(include sci.sh)
(use Main)
(use Intrface)
(use Sight)
(use Avoider)
(use Motion)
(use User)
(use System)

(public
	TurnIfSaid 0
	GoToIfSaid 1
)

(local
	local0
	local1
	local2
	egoAvoider
)
(procedure (TurnIfSaid param1 param2 param3 param4 param5 param6 &tmp temp0 temp1)
	(asm
		pushi    102
		pushi    4
		lsp      param1
		lsp      argc
		ldi      5
		ge?     
		bnt      code_0028
		lap      param5
		bnt      code_0028
		lap      param5
		jmp      code_002b
code_0028:
		lofsa    '*/*'
code_002b:
		push    
		pushi    1
		pushi    0
		lofsa    ISSc
		send     12
		sat      temp1
		push    
		dup     
		ldi      1
		eq?     
		bnt      code_0069
		pushi    252
		pushi    4
		pushi    1
		lsp      param3
		callk    IsObject,  2
		bnt      code_0050
		class    75
		jmp      code_0052
code_0050:
		class    MoveTo
code_0052:
		push    
		lsp      param3
		lsp      param4
		lofsa    ISSc
		push    
		pushi    253
		pushi    1
		class    Avoid
		push    
		lag      ego
		send     18
		jmp      code_007f
code_0069:
		dup     
		ldi      2
		eq?     
		bnt      code_007f
		lsp      argc
		ldi      6
		ge?     
		bnt      code_007f
		pushi    0
		&rest    param6
		calle    Print,  0
code_007f:
		toss    
		lat      temp1
		ret     
	)
)

(procedure (GoToIfSaid param1 param2 param3 &tmp temp0)
	(asm
		pushi    4
		dup     
		pushi    0
		lag      ego
		send     4
		push    
		pushi    #y
		pushi    0
		lag      ego
		send     4
		push    
		pushi    #x
		pushi    0
		lap      param1
		send     4
		push    
		pushi    #y
		pushi    0
		lap      param1
		send     4
		push    
		callk    GetAngle,  8
		sat      temp0
		pushi    1
		pushi    102
		pushi    4
		lsp      param1
		lsp      argc
		ldi      3
		ge?     
		bnt      code_00bf
		lap      param3
		jmp      code_00c2
code_00bf:
		lofsa    '*/*'
code_00c2:
		push    
		pushi    3
		lsp      param1
		lsg      ego
		pushi    360
		pushi    2
		pushi    4
		pushi    1
		lsg      ego
		callk    NumLoops,  2
		push    
		ldi      4
		div     
		push    
		ldi      4
		mul     
		push    
		calle    Max,  4
		div     
		push    
		calle    CantBeSeen,  6
		push    
		pushi    1
		lofsa    ISSc
		send     12
		eq?     
		bnt      code_0140
		pushi    1
		lsl      local2
		callk    IsObject,  2
		bnt      code_0107
		pushi    #dispose
		pushi    0
		lal      local2
		send     4
code_0107:
		pushi    #looper
		pushi    0
		lag      ego
		send     4
		sal      local2
		pushi    #looper
		pushi    1
		pushi    0
		pushi    58
		pushi    1
		lst      temp0
		pushi    252
		pushi    1
		pushi    0
		pushi    241
		pushi    1
		class    67
		push    
		lag      ego
		send     24
		pushi    #doit
		pushi    3
		lsg      ego
		lst      temp0
		lofsa    ISSc
		push    
		pushi    #looper
		pushi    0
		lag      ego
		send     4
		send     10
		ldi      1
code_0140:
		ret     
	)
)

(instance ISSc of Script
	(properties)
	
	(method (init param1 param2 param3 param4)
		(return
			(if param3
				(if
					(and
						(not (if param4 local0 else local1))
						(Said param2)
					)
					(if (User canControl:)
						(if (IsObject egoAvoider) (egoAvoider dispose:))
						(= egoAvoider (ego avoider?))
						(ego avoider: 0)
						(if param4 (= local0 param1) else (= local1 param1))
						(User canControl: 0 canInput: 0)
						1
					else
						((User curEvent?) claimed: 0)
						2
					)
				)
			else
				0
			)
		)
	)
	
	(method (cue &tmp newEvent)
		(User canControl: TRUE canInput: TRUE)
		((= newEvent (Event new:)) type: 128)
		(Parse (User inputLineAddr?) newEvent)
		(ego setAvoider: egoAvoider)
		(= egoAvoider 0)
		(if local0
			((ego looper?) dispose:)
			(ego looper: local2)
			(= local2 0)
			(local0 handleEvent: newEvent)
			(= local0 0)
		else
			(local1 handleEvent: newEvent)
			(= local1 0)
		)
		(if (not (newEvent claimed?))
			(regions eachElementDo: #handleEvent newEvent 1)
			(theGame handleEvent: newEvent 1)
		)
		(newEvent dispose:)
	)
)
