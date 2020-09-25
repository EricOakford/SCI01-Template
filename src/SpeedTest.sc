;;; Sierra Script 1.0 - (do not remove this comment)
;
;	SPEEDTEST.SC
;
;	This is the script that checks the machine speed, then starts the game proper.
;
;
(script# SPEED)
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
	doneTime
)
(instance fred of Actor)

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
		(= machineSpeed 0)
	)
	
	(method (doit)
		(super doit:)
		(if (== (++ machineSpeed) 1)
			(= doneTime (+ 60 (GetTime)))
		)
		(if (and (u< doneTime (GetTime)) (not (self script?)))
			(cond 
				((<= machineSpeed 25)
					(= howFast slow)
				)
				((<= machineSpeed 40)
					(= howFast medium)
				)
				((<= machineSpeed 60)
					(= howFast fast)
				)
				(else
					(= howFast fastest)
				)
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
	
	(method (changeState newState &tmp nextRoom [str 20])
		(switch (= state newState)
			(0
				(if debugging
					(Printf "machineSpeed is %d" machineSpeed)
					(Printf "howFast is %d" howFast)
				)				
				(= cycles 1)
			)
			(1
				(theGame setSpeed: 6)
				(= cycles 1)
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
						(if str
							(= nextRoom (ReadNumber @str))
						)
						(if (> nextRoom NULL)
							(break)
						)
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
