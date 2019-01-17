;;; Sierra Script 1.0 - (do not remove this comment)
(script# 952)
(include sci.sh)
(use Main)
(use Intrface)
(use System)

(public
	sysLogger 0
)

(procedure (localproc_000c param1 &tmp temp0)
	(= temp0 (GetTime 2))
	(Format
		param1
		{%02d:%02d:%02d}
		(>> temp0 $000b)
		(& (>> temp0 $0005) $003f)
		(* (& temp0 $001f) 2)
	)
	(return param1)
)

(procedure (localproc_003c param1 &tmp temp0)
	(= temp0 (GetTime 3))
	(Format
		param1
		{%02d/%02d/%02d}
		(& (>> temp0 $0005) $000f)
		(& temp0 $001f)
		(+ 80 (>> temp0 $0009))
	)
	(return param1)
)

(procedure (localproc_006c param1)
	(if (IsObject (ego mover?))
		(StrCat param1 ((ego mover?) name?))
		(StrCat param1 {_})
	)
)

(procedure (localproc_0099 param1)
	(if (IsObject (ego cycler?))
		(StrCat param1 ((ego cycler?) name?))
		(StrCat param1 {_})
	)
)

(procedure (localproc_00c8 param1 &tmp [temp0 10])
	(if (IsObject (ego script?))
		(StrCat param1 ((ego script?) name?))
		(StrCat
			param1
			(Format @temp0 { in state %d } ((ego script?) state?))
		)
	)
)

(instance sysLogger of Code
	(properties)
	
	(method (doit param1 param2 &tmp temp0 temp1 [temp2 200] [temp202 10] [temp212 10] temp222 [temp223 9] [temp232 30])
		(if (not (StrLen param1))
			(StrCpy @temp232 {})
			(GetInput
				@temp232
				30
				{Enter drive letter and your name...}
			)
			(StrCat @temp232 {.log})
			(StrCpy param1 @temp232 19)
			(= temp0 (FileIO 0 param1 0))
			(FileIO 6 temp0 {CONFIGURATION~})
			(FileIO 6 temp0 param1)
			(FileIO 6 temp0 {~})
			(= temp1 (FileIO 0 {resource.cfg} 1))
			(while (FileIO 5 @temp2 80 temp1)
				(FileIO 6 temp0 @temp2)
				(FileIO 6 temp0 {_})
			)
			(FileIO 6 temp0 {\0D\n\0D\n})
			(FileIO 1 temp1)
			(FileIO 1 temp0)
		)
		(= temp0 (FileIO 0 param1 0))
		(switch (Print {Nature of bug...}
				#button {PROG} 0
				#button {ART} 1
				#button {DESIGN} 2)
			(0 (= temp222 {PROG}))
			(1 (= temp222 { ART_}))
			(2 (= temp222 {DESIGN}))
		)
		(Format
			@temp2
			{%03d~%s~%s~%s~%s~%s \n~}
			curRoomNum
			version
			(localproc_003c @temp212)
			(localproc_000c @temp202)
			temp222
			param1
		)
		(FileIO 6 temp0 @temp2)
		(Format
			@temp2
			{Ego at %d/%d H:%d V:%d L:%d C:%d SIGNAL:%04x }
			(ego x?)
			(ego y?)
			(ego heading?)
			(ego view?)
			(ego loop?)
			(ego cel?)
			(ego signal?)
		)
		(localproc_006c @temp2)
		(localproc_0099 @temp2)
		(localproc_00c8 @temp2)
		(if (IsObject param2) (param2 doit: (StrEnd @temp2)))
		(StrCat @temp2 {\0D\n~})
		(FileIO 6 temp0 @temp2)
		(repeat
			(= temp2 0)
			(GetInput @temp2 50 {Log Entry...})
			(if (== (StrLen @temp2) 0)
				(FileIO 6 temp0 {~\0D\n\0D\n})
				(FileIO 1 temp0)
				(break)
			)
			(FileIO 6 temp0 @temp2)
			(FileIO 6 temp0 {\0D\n})
		)
		(DisposeScript 952)
	)
)
