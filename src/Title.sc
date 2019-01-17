;;; Sierra Script 1.0 - (do not remove this comment)
(script# TITLE)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Game)
(use Menu)
(use User)
(use System)

(public
	title 0
)

(instance title of Room
	(properties
		picture pSpeedTest
		style DISSOLVE
	)
	
	(method (init)
		(super init: &rest)
		;Prevent the menu bar and status line from appearing in the title
		(TheMenuBar state: DISABLED)
		(StatusLine disable:)
		(User canInput: FALSE canControl: FALSE)
		(self setScript: titleScreen)
	)
)


(instance titleScreen of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 
				(Display
					"Intro/Opening screen"
					dsCOORD 90 80
					dsCOLOR clWHITE
					dsBACKGROUND clTRANSPARENT
					)
			)
			(1
				;When starting the game proper, enable the status line and menu bar.
				(StatusLine enable:)
				(TheMenuBar state: ENABLED)
				(HandsOn)
				(curRoom newRoom: 2)
			)
		)
	)
	(method (handleEvent event)
		(super handleEvent: event)
		(switch (event type?)
			(keyDown
				(titleScreen cue:)
			)
			(mouseDown
				(titleScreen cue:)
			)
		)
	)
)