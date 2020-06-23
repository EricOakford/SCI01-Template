;;; Sierra Script 1.0 - (do not remove this comment)
(script# TITLE)
(include game.sh)
(use Main)
(use Intrface)
(use Game)
(use Menu)
(use User)
(use Actor)
(use System)

(public
	title 0
)

(local
	selectedButton
)

(enum	;highlighted buttons
	pressSTART
	pressRESTORE
	pressQUIT
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
		(directionHandler add: self)
		(TheMenuBar state: FALSE)
		
		;set up the buttons
		(newGame init:)
		(restoreGame init:)
		(quitGame init:)
		(highlight init:)
		(ShowButtonText)
		(= selectedButton pressSTART)
		(HighlightButton)

		(self setScript: titleScreen)
	)
	
	(method (dispose)
		(keyDownHandler delete: self)
		(mouseDownHandler delete: self)
		(directionHandler delete: self)
		(User canControl: TRUE)
		(super dispose:)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(direction
				(switch (event message?)
					(dirW
						(PreviousButton)
					)
					(dirE
						(NextButton)
					)					
				)
				(HighlightButton)
				(RedrawCast)				
				(event claimed: TRUE)
			)
			(keyDown
				(switch (event message?)
					(ENTER
						(titleScreen changeState: pressedAButton)
					)
					(SHIFTTAB
						(PreviousButton)
					)
					(TAB
						(NextButton)
					)
				)
				(HighlightButton)
				(RedrawCast)				
				(event claimed: TRUE)
			)
			(mouseDown
				(cond
					((MousedOn newGame event)
						(= selectedButton pressSTART)
					)
					((MousedOn restoreGame event)
						(= selectedButton pressRESTORE)
					)
					((MousedOn quitGame event)
						(= selectedButton pressQUIT)
					)
					(else ;didn't click on any buttons
						(return TRUE)
					)
				)
				(HighlightButton)
				(RedrawCast)
				(titleScreen changeState: pressedAButton)
			)
		)
		(super handleEvent: event)
	)	
)

(enum
	showTitle
	pressedAButton
	onWeGo
)

(instance titleScreen of Script
	
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
			(pressedAButton
				(switch selectedButton
					(pressSTART
						(self cue:)
					)
					(pressRESTORE
						(theGame restore:)
					)
					(pressQUIT
						(= quit TRUE)
					)				
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
)

(procedure (ShowButtonText)
	(RedrawCast)
	(Display  "Start Game"
		p_at 30 175
		p_font 4
		p_color vYELLOW
	)
	(Display "Restore Game"
		p_at 125 175
		p_font 4
		p_color vYELLOW
	)
	(Display "Quit Game"
		p_at 230 175
		p_font 4
		p_color vYELLOW
	)
)

(procedure (HighlightButton)
	(switch selectedButton
		(pressSTART
			(highlight
				posn: (newGame x?) (newGame y?)
				forceUpd:
			)
		)
		(pressRESTORE
			(highlight
				posn: (restoreGame x?) (restoreGame y?)
				forceUpd:
			)
		)
		(pressQUIT
			(highlight
				posn: (quitGame x?) (quitGame y?)
				forceUpd:
			)			
		)
	)
)

(procedure (NextButton)
	(if (== selectedButton pressQUIT)
		(= selectedButton pressSTART)
	else
		(++ selectedButton)
	)	
)

(procedure (PreviousButton)
	(if (== selectedButton pressSTART)
		(= selectedButton pressQUIT)
	else
		(-- selectedButton)
	)	
)

(instance newGame of View
	(properties
		view vTitleButton
		x 10
		y 172
	)
)

(instance restoreGame of View
	(properties
		view vTitleButton
		x 110
		y 172
	)
)

(instance quitGame of View
	(properties
		view vTitleButton
		x 210
		y 172
	)
)

(instance highlight of View
	(properties
		view vTitleButton
		loop 1
		signal (| ignrAct fixedCel fixedLoop)
	)
)