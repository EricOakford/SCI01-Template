;;; Sierra Script 1.0 - (do not remove this comment)
(script# 962)
(include sci.sh)

(public
	QScript 0
)

(procedure (QScript param1 param2 param3 param4 &tmp temp0 temp1 temp2 temp3)
	(asm
		lap      param1
		sat      temp0
		ldi      0
		sat      temp1
		ldi      0
		sat      temp2
		lsp      argc
		ldi      3
		ge?     
		bnt      code_0049
		lap      param3
		sat      temp1
		lsp      argc
		ldi      4
		ge?     
		bnt      code_0032
		lap      param4
		sat      temp2
code_0032:
		pushi    #caller
		pushi    1
		lst      temp1
		pushi    130
		pushi    1
		lst      temp2
		pushi    1
		lsp      param2
		call     localproc_0176,  2
		sap      param2
		send     12
code_0049:
		ldi      1
		bnt      code_0175
		lat      temp0
		not     
		bnt      code_006a
		pushi    #setScript
		pushi    1
		pushi    1
		lsp      param2
		call     localproc_0176,  2
		push    
		lap      param1
		send     6
		jmp      code_0175
		jmp      code_0049
code_006a:
		pushi    #respondsTo
		pushi    1
		pushi    68
		lat      temp0
		send     6
		not     
		bnt      code_00a5
		pushi    #script
		pushi    0
		lat      temp0
		send     4
		bnt      code_008f
		pushi    #script
		pushi    0
		lat      temp0
		send     4
		sat      temp0
		jmp      code_0049
code_008f:
		pushi    #setScript
		pushi    1
		pushi    1
		lsp      param2
		call     localproc_0176,  2
		push    
		lat      temp0
		send     6
		jmp      code_0175
		jmp      code_0049
code_00a5:
		lst      temp0
		lap      param1
		eq?     
		bnt      code_00ce
		pushi    #script
		pushi    0
		lap      param1
		send     4
		sat      temp0
		push    
		pushi    #script
		pushi    0
		send     4
		eq?     
		bnt      code_0049
		pushi    #new
		pushi    0
		lat      temp0
		send     4
		sat      temp0
		jmp      code_0049
code_00ce:
		pushi    #next
		pushi    0
		lat      temp0
		send     4
		bnt      code_013d
		pushi    1
		pushi    #next
		pushi    0
		lat      temp0
		send     4
		push    
		call     localproc_0176,  2
		sat      temp3
		pushi    68
		pushi    1
		lst      temp0
		pushi    #next
		pushi    0
		lat      temp0
		send     4
		eq?     
		bnt      code_010a
		pushi    #next
		pushi    1
		pushi    0
		pushi    109
		pushi    0
		pushi    #new
		pushi    0
		lat      temp3
		send     4
		send     10
		jmp      code_012c
code_010a:
		pushi    #-info-
		pushi    0
		lat      temp0
		send     4
		push    
		ldi      1
		and     
		bnt      code_012a
		pushi    #next
		pushi    1
		pushi    0
		pushi    109
		pushi    0
		pushi    #new
		pushi    0
		lat      temp3
		send     4
		send     10
		jmp      code_012c
code_012a:
		lat      temp3
code_012c:
		push    
		lat      temp0
		send     6
		pushi    #next
		pushi    0
		lat      temp0
		send     4
		sat      temp0
		jmp      code_0049
code_013d:
		pushi    68
		pushi    1
		lsp      param2
		lat      temp0
		eq?     
		bt       code_015e
		pushi    1
		lsp      param2
		callk    IsObject,  2
		bnt      code_0168
		pushi    #-info-
		pushi    0
		lap      param2
		send     4
		push    
		ldi      1
		and     
		bnt      code_0168
code_015e:
		pushi    #new
		pushi    0
		lap      param2
		send     4
		jmp      code_016a
code_0168:
		lap      param2
code_016a:
		push    
		lat      temp0
		send     6
		jmp      code_0175
		jmp      code_0049
code_0175:
		ret     
	)
)

(procedure (localproc_0176 param1)
	(if (IsObject param1) param1 else (ScriptID param1))
)
