;;; Sierra Script 1.0 - (do not remove this comment)
(script# DEBUG)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Intrface)
(use PolyEdit)
(use WriteFtr)
(use Save)
(use User)
(use Actor)
(use System)

(public
	debugRm 0
)

(instance debugRm of Script
	(properties)
	
	(method (handleEvent event &tmp newEvent temp1 [temp2 2] castFirst [temp5 80] temp85 temp86)
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
								(Format @temp5 {%d/%d} (event x?) (event y?))
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
					(KEY_ALT_t
						(if
							(and
								(> 105 (= temp85 (GetNumber {Which room number?})))
								(> temp85 0)
							)
							(curRoom newRoom: temp85)
						)
					)
					(KEY_QUESTION
						(Print {Debug Key commands:\n
							ALT-S Show cast\n
							ALT-M   Show memory\n
							ALT-T Teleport\n
							ALT-V   Visual\n
							ALT-P Priority\n
							ALT-C   Control\n
							ALT-I Get InvItem\n
							ALT-D Internal debugger\n
							ALT-E   Show ego\n} #window SysWindow)
					)
					(KEY_ALT_s
						(= castFirst (cast first:))
						(while castFirst
							(= temp1 (NodeValue castFirst))
							(Print
								(Format
									@temp5
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
					(KEY_ALT_i
						(= castFirst (GetNumber {ID number of the object?}))
						(ego get: castFirst)
					)
					(KEY_ALT_m (theGame showMem:))
					(KEY_ALT_e
						(Format
							@temp5
							{ego\n
							x:%d y:%d\n
							loop:%d\n
							cel:%d}
							(ego x?)
							(ego y?)
							(ego loop?)
							(ego cel?)
						)
						(Print @temp5 #icon (ego view?) 0 0)
					)
					(KEY_ALT_v (Show VMAP))
					(KEY_ALT_p (Show PMAP))
					(KEY_ALT_c (Show CMAP))
					(KEY_ALT_w (CreateObject doit:))
					(KEY_ALT_d (SetDebug)) ;Added to allow the interpreter's internal debugger to be accessed with a simpler key combo
					
					(else  (event claimed: FALSE))
				)
			)
		)
		(DisposeScript DEBUG)
	)
)
