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
	(method (doit roomNum &tmp event)
		(theGame setCursor: waitCursor TRUE)
		
		;clear any events and stray nodes in the event handlers
		(while ((= event (Event new:)) type?)
			(event dispose:)
		)
		(event dispose:)
		(mouseDownHandler release:)
		(keyDownHandler release:)
		(directionHandler release:)
		
		;clear any modeless dialog
		(cls)
		
		;dispose any scripts that are only occasionally used
		(LoadMany FALSE
			AVOIDER JUMP ORBIT PATH EXTRA TEXTRA RFEATURE DEMO
			NAMEFIND CHASE FOLLOW WANDER REVERSE TIMER SORT COUNT DPATH
			QSCRIPT FORCOUNT CAT TRACK GOTOSAID LASTLINK TIMEDCUE SORTCOPY
			APPROACH MOVEFWD POLYGON POLYPATH BLOCK PRINTD
			SIGHT QSOUND SMOOPER
		)
		;and finally, trash this script
		(DisposeScript DISPOSE_CODE)
	)
)