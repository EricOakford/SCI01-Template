;;; Sierra Script 1.0 - (do not remove this comment)
;**
;**	Sierra On-line basic standard menu
;**
;**		by Al Lowe
;**
;**		adapted by Pablo Ghenis
;**
;**	Last Update:	August 26, 1988
;**


;**	Break lines in "AddMenu" before a divider, for aesthetics
;**	= sets a menu item's starting value
;**	! makes the item non-selectable
;**	` denotes the following character as the key for the menu
;**	: separates menu items within a menu stack


(script# MENU)									;**	output to script.997
(use Main)
(use Intrface)
(use PrintD)
(use Gauge)
(use User)
(include game.sh) (include menu.sh)

;moved enums to MENU.SH per "SCI Changes and Updates" document
;entry 09/05/1988. This will allow other scripts to use the menu defines.

(procedure (SetInputText event)
	(if (> argc 1) (Format (User inputLineAddr?) &rest))
	(event claimed: FALSE type: keyDown message: (User echo?))
)


(class TheMenuBar kindof MenuBar			;**	MENUBAR
	(method (init)
		(AddMenu { \01 }
			{About game`^a:Help`#1}
		)

		(AddMenu { File }
			{Save Game`#5:Restore Game`#7:--!
			:Restart Game`#9:Quit`^q}
		)

		(AddMenu { Action }
			{Pause Game`^p:Inventory`^I:Retype`#3}
		)

		(AddMenu { Speed }
			{Change...`^s:--!:Faster`+:Normal`=:Slower`-:--!
			:Detail Level`#6}
		)

		(AddMenu { Sound }
			{Volume...`^v:Sound Off`#2=1}
		)
		(SetMenu soundI
			#text
				(if (DoSound SoundOn)
					{Turn sound off}
				else
					{Turn sound on}
				)
		)


		(SetMenu saveI			p_said 'save[/game]')
		(SetMenu restoreI		p_said 'restore[/game]')
		(SetMenu restartI		p_said 'restart[/game]')
		(SetMenu quitI			p_said 'quit[/game]')
		(SetMenu	pauseI		p_said 'pause[/game]')
		(SetMenu invI			p_said 'inventory')
	)


	(method (handleEvent event &tmp i oldPause but1 but2 but3 but4 [str 250])
		(switch (super handleEvent: event)


			;**************		SIERRA MENU		**************

			(aboutI
				(Print
					(Format @str
						"SCI01 Template Game\n
						By Eric Oakford\n\n
						Version %s" version ;this brings up the version number defined in MAIN.SC.
					)
						#title "About"
				)
				(Print
					(Format @str
						"You've been playing for %d hours, %d minutes, and %d seconds."
						gameHours gameMinutes gameSeconds
					)
				)
			)
			(helpI
				(Print  "DURING THE GAME:\n
							ESC opens and closes the menus,\n
							which show additional shortcuts.\n\n
							
							IN DIALOG WINDOWS:\n
							Your current choice is outlined.\n
							Tab and Shift-Tab move between\n
							choices.\n
							ESC always cancels.\n\n

							IN TYPING WINDOWS:\n
							Arrows, Home, End and ctrl-Arrows\n
							move the cursor.\n
							Ctrl-C clears the line."

							#title	{Help}
							#font		smallFont
				)
			)


			;**************		FILE MENU		**************

			(saveI
				(theGame save:)
			)

			(restoreI
				(theGame restore:)
			)

			(restartI
				(if
					(Print "You mean you want to start over again
						from the very beginning?"
							#title	{Restart}
							#font		bigFont
							#button	{Restart} 1
							#button	{Oops} 0
					)
					(theGame restart:)
				)
			)

			(quitI
				(= quit
					(Print "Are you just going to quit and
						leave me here all alone like this?"
							#title	{Quit}
							#font		bigFont
							#button	{Quit} 1
							#button	{Oops} 0
					)
				)
			)


			;**************		ACTION MENU		**************

			(pauseI
				(Print "Sure, you go ahead.
					I'll just wait in here until you get back..."
							#title	{This game is paused.}
							#font		bigFont
							#button	{Ok. I'm back.} 1
				)
			)

			(invI
				(inventory showSelf:	ego)
			)

			(repeatI
				(SetInputText event)
			)


			;**************		SPEED MENU		**************

			(speedI
				(= i
					((Gauge new:)
						description:
							{Use the mouse or right and left arrow keys to
							select the speed at which characters move.}
						text: {Animation Speed}
						minimum: 0
						normal: 10
						maximum: 15
						higher: {Faster}
						lower: {Slower}
						doit:(- 16 speed)
					)
				)
				(theGame setSpeed:(- 16 i))
				(DisposeScript GAUGE)
			)
				
			(fasterI
				(if (> speed (^ 1 (= i debugging)))	;**	This lets Al haul ass!
					(theGame setSpeed: (-- speed))
				)
			)

			(normalI
				(theGame setSpeed: 6)
			)

			(slowerI
				(theGame setSpeed: (++ speed))
			)
			
			(detailI
				(= but1 {Low})
				(= but2 {Medium})
				(= but3 {High})
				(= but4 {Ultra})
				(switch dftHowFast
					(slow
						(= but1 {Optimal})
					)
					(medium
						(= but2 {Optimal})
					)
					(fast
						(= but3 {Optimal})
					)
					(fastest
						(= but4 {Optimal})
					)
				)
				(= i
					(PrintD
						#title {Game Detail Level}
						#button but1
						#button but2
						#button but3
						#button but4
						#first i
					)
				)
				(if i
					(= howFast (- i 2))
					(if (and (== howFast fast) (>= dftHowFast fastest))
						(= howFast dftHowFast)
					)
				)
				(if debugging
					(Printf {howFast is now %d. dftHowFast is %d.} howFast dftHowFast)
				)
			)

			;**************		SOUND MENU		**************

			(volumeI
				(= i
					((Gauge new:)
						description:
							{Use the mouse or right and left arrow keys to
							set the sound volume.}
						text: {Sound Volume}
						minimum: 0
						normal: 12
						maximum: 15
						higher: {Louder}
						lower: {Softer}
						doit: (DoSound MasterVol)
					)
				)
				(DoSound MasterVol i)
				(DisposeScript GAUGE)
			)
				
			(soundI
				(= i (DoSound SoundOn))
				(SetMenu soundI
					p_text (if i {Turn sound on} else {Turn sound off})
				)
				(DoSound SoundOn (not i))
			)
		)
	)
)