;;; Sierra Script 1.0 - (do not remove this comment)
;**
;**   Sierra On-line basic standard menu
;**
;**      by Al Lowe
;**
;**      adapted by Pablo Ghenis
;**
;**   Last Update:   August 26, 1988
;**


;**   Break lines in "AddMenu" before a divider, for aesthetics
;**   = sets a menu item's starting value
;**   ! makes the item non-selectable
;**   ` denotes the following character as the key for the menu
;**   : separates menu items within a menu stack


(script# MENU)                         ;**   output to script.997
(include game.sh) (include menu.sh)
(use Main)
(use Intrface)
(use Sound)
(use User)
(use Gauge)

(public
	SetInputText 1
)
;moved enums to MENU.SH, as per "SCI Changes and Updates" document
;entry 09/05/1988. This will allow other scripts to use the menu defines.

(procedure (SetInputText event)
	(if (> argc 1) (Format (User inputLineAddr?) &rest))
	(event claimed: FALSE type: keyDown message: (User echo?))
)
(class TheMenuBar kindof MenuBar       ;**   MENUBAR
   (method (init)
      (AddMenu { \01 }
         { About game`^a: Help`#1}
      )

      (AddMenu { File }
         { Save Game`#5: Restore Game`#7:--!
         : Restart Game`#9: Quit`^q}
      )

      (AddMenu { Action }
         { Pause Game`^p: Inventory`^I: Retype`#3:--!
         : Colors}
	  )

      (AddMenu { Speed }
         { Change...`^s:--!: Faster`+: Normal`=: Slower`-}
      )

      (AddMenu { Sound }
         { Volume...`^v: Sound Off`#2=1}
      )
      (SetMenu soundI
        #text
            (if (DoSound SoundOn)
               { Sound off}
            else
               { Sound on}
            )
     )
     (if (< (Graph GDetect) 9)
			(SetMenu colorI 32 0)
		else
			(SetMenu colorI 109 '/color')
	 )

      (SetMenu saveI       109 'save[/game]')
      (SetMenu restoreI    109 'restore[/game]')
      (SetMenu restartI    109 'restart[/game]')
      (SetMenu quitI       109 'quit[/game]')
      (SetMenu pauseI      109 'pause[/game]')
      (SetMenu invI        109 'inventory')
      (SetMenu normalI     109 'normal')
      (SetMenu fasterI     109 'faster')
      (SetMenu slowerI     109 'slower')
;      (SetMenu teleportI   109 'tp')
   )

   (method (handleEvent event &tmp i newBackColor newTextColor [str 300])
      (switch (super handleEvent: event (User blocks?))

         ;**************      SIERRA MENU    **************

         (aboutI
			(Print (Format @str
					"SCI01 Template Game\n
					By Eric Oakford\n
					Version %s" version) ;this brings up the version number defined in MAIN.SC.
			#title "About"
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

                     #title   "Help"
                     #font    smallFont
            )
         )



         ;**************      FILE MENU      **************

         (saveI
            (theGame save:)
         )

         (restoreI
            (theGame restore:)
         )

         (restartI
            (if
				(Print "You mean you want to start over again from the very beginning?"
				#title   {Restart}
				#font    bigFont
				#button  {Restart} 1
				#button  {Oops} 0
				)
		(theGame restart:)
			)
         )

         (quitI
			(= quit
				(Print "Are you just going to quit and leave me here all alone like this?"
					#title   {Quit}
					#font    bigFont
					#button  {Quit} 1
					#button  {Oops} 0
				)
			)
		 )

         ;**************      ACTION MENU    **************

         (pauseI
            (Print "Sure, you go ahead.
               I'll just wait in here until you get back..."
                     #title   {This game is paused.}
                     #font    bigFont
                     #button  {Ok. I'm back.} 1
            )
         )

         (invI
            (inventory showSelf: ego)
         )

         (repeatI
         	(SetInputText event)
		 )
		(colorI
			(= newTextColor 16)
			(while (and (u> newTextColor 15) (!= newTextColor -1))
				(= newTextColor (GetNumber {New Text Color: (0-15)}))
			)
			(if (!= newTextColor -1)
				(= newBackColor 16)
				(while
					(and
						(!= newBackColor -1)
						(or (u> newBackColor 15) (== newBackColor newTextColor))
					)
					(= newBackColor (GetNumber {New Background Color: (0-15)}))
				)
				(if (!= newBackColor -1)
					(= curTextColor newTextColor)
					(= curBackColor newBackColor)
				)
			)
			(systemWindow color: curTextColor back: curBackColor)
		)		 

         ;**************      SPEED MENU     ************** 
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
                  doit: (- 16 speed)
               )
            )
            (theGame setSpeed:(- 16 i))
            (DisposeScript GAUGE)
         )

			(fasterI
				(if (> speed 0) (theGame setSpeed: (-- speed)))
			)
			(normalI (theGame setSpeed: 6))
			(slowerI
				(if (< speed 16)
					(theGame setSpeed: (++ speed))
				)
			)
			         ;**************      SOUND MENU     **************

			(volumeI
				(if
					(!=
						(= i
							(GetNumber {Volume (1 - 16)?} (+ 1 (DoSound MasterVol)))
						)
						-1
					)
					(if (< (-- i) 0) (= i 0))
					(if (> i 15) (= i 15))
					(DoSound MasterVol i)
				)
			)
         (soundI
            (= i (DoSound SoundOn))
            (if (GetMenu soundI p_value)
					(DoSound SoundOn FALSE)
					(SetMenu soundI p_value FALSE p_text { Sound on})
				else
					(DoSound SoundOn TRUE)
					(SetMenu soundI p_value TRUE p_text {Sound off})
            )
            (DoSound SoundOn (not i))
         )

      )
   )
)