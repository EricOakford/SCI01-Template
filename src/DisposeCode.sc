;;; Sierra Script 1.0 - (do not remove this comment)
;	DISPOSECODE.SC
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
	
	(method (doit roomNum)
		(LoadMany FALSE
			AVOIDER JUMP ORBIT PATH EXTRA TEXTRA RFEATURE DEMO
			NAMEFIND CHASE FOLLOW WANDER REVERSE TIMER SORT COUNT DPATH
			QSCRIPT FORCOUNT CAT TRACK GOTOSAID LASTLINK TIMEDCUE SORTCOPY
			APPROACH MOVEFWD POLYGON POLYPATH BLOCK PRINTD
			SIGHT QSOUND SMOOPER
		)
		(DisposeScript DISPOSE_CODE)
	)
)