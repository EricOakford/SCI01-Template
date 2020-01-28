;;; Sierra Script 1.0 - (do not remove this comment)
(script# LOGGER)
(include game.sh)
(use Main)
(use Intrface)
(use System)

(public
	sysLogger 0
)

(procedure (GetTimeStr str &tmp tm)
	(= tm (GetTime SYSTIME2))
	(Format str LOGGER 0
		(>> tm 11)
		(& (>> tm 5) %111111)
		(* (& tm %11111) 2)
	)
	(return str)
)

(procedure (GetDateStr str &tmp tm)
	(= tm (GetTime SYSDATE))
	(Format str LOGGER 1
		(& (>> tm 5) %1111)
		(& tm %11111)
		(+ 80 (>> tm 9))
	)
	(return str)
)

(procedure (AddMover str)
	(if (IsObject (ego mover?))
		(StrCat str ((ego mover?) name?))
		(StrCat str {_})
	)
)

(procedure (AddCycler str)
	(if (IsObject (ego cycler?))
		(StrCat str ((ego cycler?) name?))
		(StrCat str {_})
	)
)

(procedure (AddScript str &tmp [fmt 10])
	(if (IsObject (ego script?))
		(StrCat str ((ego script?) name?))
		(StrCat
			str
			(Format @fmt LOGGER 2 ((ego script?) state?))
		)
	)
)

(instance sysLogger of Code
	(properties)
	
	(method (doit path callBack &tmp logHandle cfgHandle [str 200] [timeStr 10] [_dateStr 10] whoTo [tmpPath 30])
		(if (not (StrLen path))
			(StrCpy @tmpPath {})
			(GetInput @tmpPath 30 {Enter drive letter and your name...})
			(StrCat @tmpPath {.log})
			(StrCpy path @tmpPath 19)
			(= logHandle (FileIO fileOpen path fAppend))
			(FileIO fileFPuts logHandle {CONFIGURATION~})
			(FileIO fileFPuts logHandle path)
			(FileIO fileFPuts logHandle {~})
			(= cfgHandle (FileIO fileOpen {resource.cfg} fRead))
			(while (FileIO fileFGets @str 80 cfgHandle)
				(FileIO fileFPuts logHandle @str)
				(FileIO fileFPuts logHandle {_})
			)
			(FileIO fileFPuts logHandle {\0D\n\0D\n})
			(FileIO fileClose cfgHandle)
			(FileIO fileClose logHandle)
		)
		(= logHandle (FileIO fileOpen path fAppend))
		(switch
			(Print LOGGER 3
				#button {PROG} 0
				#button {ART} 1
				#button {DESIGN} 2
			)
			(0 (= whoTo {PROG}))
			(1 (= whoTo { ART_}))
			(2 (= whoTo {DESIGN}))
		)
		(Format @str LOGGER 4
			curRoomNum
			version
			(GetDateStr @_dateStr)
			(GetTimeStr @timeStr)
			whoTo
			path
		)
		(FileIO fileFPuts logHandle @str)
		(Format @str LOGGER 5
			(ego x?)
			(ego y?)
			(ego heading?)
			(ego view?)
			(ego loop?)
			(ego cel?)
			(ego signal?)
		)
		(AddMover @str)
		(AddCycler @str)
		(AddScript @str)
		(if (IsObject callBack)
			(callBack doit: (StrEnd @str))
		)
		(StrCat @str {\0D\n~})
		(FileIO fileFPuts logHandle @str)
		(repeat
			(= str 0)
			(GetInput @str 50 {Log Entry...})
			(if (== (StrLen @str) 0)
				(FileIO fileFPuts logHandle {~\0D\n\0D\n})
				(FileIO fileClose logHandle)
				(break)
			)
			(FileIO fileFPuts logHandle @str)
			(FileIO fileFPuts logHandle {\0D\n})
		)
		(DisposeScript LOGGER)
	)
)
