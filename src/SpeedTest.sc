;;; Sierra Script 1.0 - (do not remove this comment)
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
	local0
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
			view: 98
			setLoop: 0
			illegalBits: 0
			posn: 20 99
			setStep: 1 1
			setMotion: MoveTo 300 100
			setCycle: Forward
			init:
		)
		(theGame setSpeed: 0)
		(= speedCount 0)
	)
	
	(method (doit)
		(super doit:)
		(if (== (++ speedCount) 1)
			(= local0 (+ 60 (GetTime)))
		)
		(if
		(and (u< local0 (GetTime)) (not (self script?)))
			(cond 
				((<= speedCount 25) (= detailLevel 0))
				((<= speedCount 40) (= detailLevel 1))
				((<= speedCount 60) (= detailLevel 2))
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
	
	(method (changeState newState &tmp nextRoom [inputRoom 20])
		(switch (= state newState)
			(0 (= cycles 1))
			(1
				(theGame setSpeed: 6)
				(= cycles (if debugging 1 else 30))
			)
			(2
				(if debugging
					(repeat
						(= inputRoom NULL)
						(= nextRoom
							(Print "Where to, boss?"
								#edit @inputRoom 5
								#window SysWindow
							)
						)
						(if inputRoom (= nextRoom (ReadNumber @inputRoom)))
						(if (> nextRoom 0) (break))
					)
				else
					(= nextRoom TITLE)
					(TheMenuBar state: DISABLED)
				)
				(TheMenuBar state: ENABLED)
				(HandsOn)
				(curRoom newRoom: nextRoom)
			)
		)
	)
)
