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
(use Intrface)
(use Save)
(use User)
(use System)

(public
	gameInitCode 0
	driverInit 1
)

(instance gameInitCode of Code
	(method (doit)
		(= debugging TRUE) ;Set to TRUE if you want to enable the debug features.
		(= possibleScore 0)	;Set the maximum score here
		(= useSortedFeatures TRUE)
		(driverInit doit:)
		(DoSound MasterVol volume)	;ensure that the initial volume is the default
		(DisposeScript GAME_INIT)	;and finally, trash this script from memory
	)
)

(instance driverInit of Code
	;initialize the colors and sound based on the graphics and sound drivers
	(method (doit &tmp dftStyle)
		;initialize number of voices the sound driver supports
		(= numVoices (DoSound NumVoices))
		;initialize the number of colors the graphics driver supports	
		(= numColors (Graph GDetect))
		(cond
			((<= numColors 8)	;is CGA
				(= myTextColor vBLACK)
				(= myBackColor vWHITE)
				(= dftStyle HSHUTTER)
			)
			((<= numColors 16)	;is EGA
				(= myTextColor vBLACK)
				(= myBackColor vWHITE)
				(= dftStyle HSHUTTER)
			)
			(else	;is VGA
				(= isVGA TRUE)
				(= myTextColor 0)
				(= myBackColor 7)
				(= dftStyle FADEOUT)			
			)
		)
		(= showStyle dftStyle)
		(systemWindow
			color: myTextColor
			back: myBackColor
		)
		;here for testing different drivers
;;;		(if debugging
;;;			(Printf
;;;				"numVoices is %d\n
;;;				numColors is %d"
;;;				numVoices numColors
;;;			)
;;;		)
		(self dispose:)
	)
)