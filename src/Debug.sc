;;; Sierra Script 1.0 - (do not remove this comment)
(script# DEBUG)
(include game.sh)
(use Main)
(use Intrface)
(use Save)
(use User)
(use Game)
(use Actor)
(use System)

(public
	debugRm 0
)

(instance debugRm of Locale
	(properties)
	
	(method (handleEvent event &tmp newEvent temp1 [temp2 2] castFirst [str 80] temp85 temp86)
		(switch (event type?)
			(mouseDown
				(cond 
					((& (event modifiers?) ctrlDown)
						(event claimed: TRUE)
						(User canControl: TRUE)
						(while (!= 2 ((= newEvent (Event new:)) type?))
							(GlobalToLocal newEvent)
							(Animate (cast elements?) 0)
							(ego posn: (newEvent x?) (newEvent y?) setMotion: 0)
							(newEvent dispose:)
						)
						(newEvent dispose:)
					)
					((& (event modifiers?) shiftDown)
						(event claimed: TRUE)
						(= temp1
							(Print
								(Format @str {%d/%d} (event x?) (event y?))
								#at
								(- (event x?) 21)
								(- (event y?) 17)
								#font
								999
								#dispose
							)
						)
						(while (!= 2 ((= newEvent (Event new:)) type?))
							(newEvent dispose:)
						)
						(temp1 dispose:)
						(newEvent dispose:)
					)
				)
			)
			(keyDown
				(event claimed: TRUE)
				(switch (event message?)
					(`@t
						(if
							(and
								(> 105 (= temp85 (GetNumber {Which room number?})))
								(> temp85 0)
							)
							(curRoom newRoom: temp85)
						)
					)
					(`?
						(Print "Debug Key commands:\n
							ALT-S Show cast\n
							ALT-M   Show memory\n
							ALT-T Teleport\n
							ALT-V   Visual\n
							ALT-P Priority\n
							ALT-C   Control\n
							ALT-I Get InvItem\n
							ALT-D Internal debugger\n
							ALT-E   Show ego\n"
							#window SysWindow)
					)
					(`@s
						(= castFirst (cast first:))
						(while castFirst
							(= temp1 (NodeValue castFirst))
							(Print
								(Format
									@str
									{view: %d\n
									(x,y):%d,%d\n
									STOPUPD=%d\n
									IGNRACT=%d\n
									ILLBITS=$%x}
									(temp1 view?)
									(temp1 x?)
									(temp1 y?)
									(/ (& (temp1 signal?) $0004) 4)
									(/ (& (temp1 signal?) $4000) 16384)
									(if
										(or
											(== (temp1 superClass?) Actor)
											(== (temp1 superClass?) Ego)
										)
										(temp1 illegalBits?)
									else
										-1
									)
								)
								#window
								SysWindow
								#title
								(temp1 name?)
								#icon
								(temp1 view?)
								(temp1 loop?)
								(temp1 cel?)
							)
							(= castFirst (cast next: castFirst))
						)
					)
					(`@i
						(= castFirst (GetNumber {ID number of the object?}))
						(ego get: castFirst)
					)
					(`@m (theGame showMem:))
					(`@e
						(Format
							@str
							{ego\n
							x:%d y:%d\n
							loop:%d\n
							cel:%d}
							(ego x?)
							(ego y?)
							(ego loop?)
							(ego cel?)
						)
						(Print @str #icon (ego view?) 0 0)
					)
					(`@v (Show VMAP))
					(`@p (Show PMAP))
					(`@c (Show CMAP))
					(`@d (SetDebug))
					;Added to allow the interpreter's internal debugger
					;to be accessed with a simpler key combo
					
					(else  (event claimed: FALSE))
				)
			)
		)
		(DisposeScript DEBUG)
	)
)
