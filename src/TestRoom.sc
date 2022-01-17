;;; Sierra Script 1.0 - (do not remove this comment)
(script# TESTROOM)
(include game.sh)
(use Main)
(use Feature)
(use Game)
(use Intrface)
(use Main)
(use Actor)
(use System)

(public
	rm011 0
)

(instance rm011 of Room
	(properties
		picture pWhite
		north 0
		east 0
		south 0
		west 0
	)

	(method (init)
		(StatusLine enable:)
		(super init:)
		(HandsOn)
		(switch prevRoomNum
			(else 
				(ego posn: 146 153 loop: 1)
			)
		)
		(RedrawCast)
		(ego init:)
		(ego get: iTestObject)
	)

	(method (handleEvent event)
		(cond
			((super handleEvent: event))
			((Said 'look>')
				(cond
					((Said '[/noword,room]')
						(Print "This is an empty room.")
					)
				)
			)
		)
	)
)