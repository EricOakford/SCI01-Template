;;; Sierra Script 1.0 - (do not remove this comment)
(script# TESTROOM)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Feature)
(use Game)
(use Intrface)
(use Main)
(use System)

(public
	rm002 0
)

(instance rm002 of Room
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(StatusLine enable:)
		(super init:)
		(switch prevRoomNum
			(else 
				(ego posn: 146 153 loop: 1)
			)
		)
		(SetUpEgo)
		(ego init:)
		(ego get: iTestObject)
	)
	(method (handleEvent event)
		(super handleEvent: event)
		(switch (event type?)
			; handle Said's, etc...	
			(evSAID
				(cond
					((Said 'look>')
						(cond
							((Said '[/!*,room]')
								(Print {This is an empty room.})
							)
						)
					)
				)
			)
		)
	)
)