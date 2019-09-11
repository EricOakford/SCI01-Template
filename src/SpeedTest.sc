;;; Sierra Script 1.0 - (do not remove this comment)
;
;	SPEEDTEST.SC
;
;	This is the script that checks the machine speed, then starts the game proper.
;
;
(script# SPEEDTEST)
(include game.sh)
(use Main)
(use Intrface)
(use Save)
(use Motion)
(use Game)
(use User)
(use Menu)
(use Actor)
(use System)

(public
	speedTest 0
)

(local
	endTime
)
(instance fred of Actor
	(properties)
)

(instance speedTest of Room
	(properties
		picture pBlack
		style IRISIN
	)
	
	(method (init)
		(HandsOff)
		(super init:)
		(sounds eachElementDo: #stop)
		(fred
			view: vSpeedTest
			setLoop: 0
			illegalBits: 0
			posn: 20 99
			setStep: 1 1
			setMotion: MoveTo 300 100
			setCycle: Forward
			init:
		)
		(theGame setSpeed: 0)
		(= howFast 0)
	)
	
	(method (doit)
		(super doit:)
		(if (== (++ howFast) 1)
			(= endTime (+ 60 (GetTime)))
		)
		(if
		(and (u< endTime (GetTime)) (not (self script?)))
			(cond 
				((<= howFast 25) (= detailLevel 0))
				((<= howFast 40) (= detailLevel 1))
				((<= howFast 60) (= detailLevel 2))
				(else (= detailLevel 3))
			)
			(self setScript: speedScript)
		)
	)
	
	(method (dispose)
		(User blocks: 0)
		(super dispose:)
	)
)

(instance speedScript of Script
	(properties)
	
	(method (changeState newState &tmp nextRoom [str 20])
		(switch (= state newState)
			(0 (= cycles 1))
			(1
				(theGame setSpeed: 6)
				(= cycles (if debugging 1 else 30))
			)
			(2
				(if debugging
					(repeat
						(= str NULL)
						(= nextRoom
							(Print "Where to, boss?"
								#edit @str 5
								#window SysWindow
							)
						)
						(if str (= nextRoom (ReadNumber @str)))
						(if (> nextRoom NULL) (break))
					)
				else
					(= nextRoom TITLE)
				)
				(TheMenuBar state: TRUE)
				(curRoom newRoom: nextRoom)
			)
		)
	)
)
