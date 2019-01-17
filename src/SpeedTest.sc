;;; Sierra Script 1.0 - (do not remove this comment)
(script# SPEEDTEST)
(include system.sh) (include sci2.sh) (include game.sh)
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
		picture 10
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
				((<= speedCount 25) (= detailLevel DETAIL_LOW))
				((<= speedCount 40) (= detailLevel DETAIL_MID))
				((<= speedCount 60) (= detailLevel DETAIL_HIGH))
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
				(= cycles (if testingCheats 1 else 30))
			)
			(2
				(if testingCheats
					(repeat
						(= inputRoom 0)
						(= nextRoom
							(Print {Where to, boss?}
								#edit @inputRoom 5
								#window SysWindow
							)
						)
						(if inputRoom (= nextRoom (ReadNumber @inputRoom)))
						(if (> nextRoom 0) (break))
					)
				else
					(= nextRoom 1)
					(TheMenuBar state: ENABLED)
				)
				(TheMenuBar state: ENABLED)
				(curRoom newRoom: nextRoom)
			)
		)
	)
)
