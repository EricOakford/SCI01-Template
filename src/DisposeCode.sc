;;; Sierra Script 1.0 - (do not remove this comment)
;	DISPOSECODE>SC
;
;	 A place to put script numbers that are rarely used and should be unloaded
;	 on each room change to free up heap space. If you add a new script with a motion class, or a cycler,
;	 you should probably add your script number here.
;
;
(script# DISPOSE_CODE)
(include game.sh)
(use Main)
(use LoadMany)
(use System)

(public
	disposeCode 0
)

(instance disposeCode of Code
	(properties)
	
	(method (doit)
		(LoadMany FALSE	
			;These are all disposed when going to another room, to reduce the
			;chances of "Memory Fragmented" errors.
			EXTRA FILE QSOUND GROOPER FORCOUNT SIGHT DPATH MOVEFWD JUMP SMOOPER
			REVERSE CHASE FOLLOW WANDER POLYPATH BLOCK PRINTD EXTRA
			APPROACH AVOIDER POLYGON TIMER QSOUND
		)
		(DisposeScript DISPOSE_CODE)
	)
)