;;; Sierra Script 1.0 - (do not remove this comment)
(script# TITLE)
(include game.sh)
(use Main)
(use Intrface)
(use Game)
(use Menu)
(use User)
(use System)

(public
	title 0
)

(instance title of Room
	(properties
		picture pBlack
		style DISSOLVE
	)

	(method (init)
		(super init:)
		(keyDownHandler add: self)
		(mouseDownHandler add: self)
		(TheMenuBar state: FALSE)
		(self setScript: titleScreen)
	)
	
	(method (dispose)
		(keyDownHandler delete: self)
		(mouseDownHandler delete: self)
		(super dispose:)
	)
)

(enum
	showTitle
	onWeGo
)

(instance titleScreen of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(showTitle
				(Display
					"Intro/Opening screen"
					p_at 90 80
					p_color vWHITE
					p_back -1
				)
			)
			(onWeGo
				;When starting the game proper, enable the status line and menu bar.
				(StatusLine enable:)
				(TheMenuBar state: TRUE)
				(curRoom newRoom: TESTROOM)
			)
		)
	)
	
	(method (handleEvent event)
		(super handleEvent: event)
		(if (not (event claimed?))
			(self changeState: onWeGo)
		)
	)
)