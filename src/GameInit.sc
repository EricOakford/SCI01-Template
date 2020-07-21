;;; Sierra Script 1.0 - (do not remove this comment)
;
;	GAMEINIT.SC
;
;	Add things to initialize at game start here.
;	Make sure they don't require any objects or methods in MAIN.SC.
;
;
;	Initializing things here means that the code can be disposed at completion,
;	as it will not be used again. This was done in Quest for Glory I.
;

(script# GAME_INIT)
(include game.sh)
(use Main)
(use Window)
(use Save)
(use User)
(use System)

(public
	gameInitCode 0
)

(instance gameInitCode of Code
	(method (init)
		(= debugging TRUE) ;Set to TRUE if you want to enable the debug features.
		(DoSound MasterVol volume)	;ensure that the initial volume is the default
		(= numColors (Graph GDetect))
		(= numVoices (DoSound NumVoices))
		(= possibleScore 0)	;Set the maximum score here
		(if
			(and
				(>= (= numColors (Graph GDetect)) 2)
				(<= numColors 16)
			)
			(Bclr fIsVGA)
		else
			(Bset fIsVGA)
		)
		;initialize the colors
		(if (Btst fIsVGA)
			(= myTextColor 0)
			(= myBackColor 7)
			(= showStyle FADEOUT)
		else
			(= myTextColor vBLACK)
			(= myBackColor vWHITE)
			(= showStyle HSHUTTER)			
		)
		(systemWindow
			color: myTextColor
			back: myBackColor
		)		
		(DisposeScript GAME_INIT)	;and finally, trash this script from memory
	)
)