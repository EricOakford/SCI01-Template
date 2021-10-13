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
(use WriteFtr)

(public
	debugRm 0
)

(instance debugRm of Locale
	(method (init)
		(super init:)
		(mouseDownHandler add: self)
		(keyDownHandler add: self)
	)
	
	(method (dispose)
		(mouseDownHandler delete: self)
		(keyDownHandler delete: self)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp evt obj i [str 80] nullStr nextRoom)
		(if (or (not debugging) (event claimed?)) (return))
		(switch (event type?)
			(mouseDown
				(cond 
					((& (event modifiers?) ctrlDown)
						(event claimed: TRUE)
						(User canControl: TRUE)
						(while (!= mouseUp ((= evt (Event new:)) type?))
							(GlobalToLocal evt)
							(Animate (cast elements?) FALSE)
							(ego posn: (evt x?) (evt y?) setMotion: 0)
							(evt dispose:)
						)
						(evt dispose:)
					)
					((& (event modifiers?) shiftDown)
						(= obj
							(Print
								(Format @str "%d/%d" (event x?) (event y?))
								#at (- (event x?) 21) (- (event y?) 17)
								#font 999
								#dispose
							)
						)
						(while (!= mouseUp ((= evt (Event new:)) type?))
							(evt dispose:)
						)
						(obj dispose:)
						(evt dispose:)
					)
				)
			)
			(keyDown
				(event claimed: TRUE)
				(switch (event message?)
					(`?
						(Print "Debug Key commands:\n
							ALT-S Show cast\n
							ALT-M Show memory\n
							ALT-T Teleport\n
							ALT-V Visual\n
							ALT-P Priority\n
							ALT-C Control\n
							ALT-I Get InvItem\n
							ALT-D Internal debugger\n
							ALT-E Show ego\n
							ALT-K Kill ego\n
							ALT-W Write feature"
						)
					)
					(`@t
						(if
							(and
								(> 105 (= nextRoom (GetNumber {Which room number?})))
								(> nextRoom 0)
							)
							(curRoom newRoom: nextRoom)
						)
					)
					(`@s
						(= nullStr {*})
						(StrAt nullStr 0 0)
						(= i (cast first:))
						(while i
							(= obj (NodeValue i))
							(Print
								(Format @str
									"view: %d\n
									(x,y):%d,%d\n
									STOPUPD=%d\n
									IGNRACT=%d\n
									ILLBITS=$%x"
									(obj view?)
									(obj x?)
									(obj y?)
									(if (& (obj signal?) notUpd) {stopUpd:\n} else nullStr)
									(if (& (obj signal?) ignrAct) {ignoreActors:\n} else nullStr)
									(if
										(or
											(== (obj superClass?) Actor)
											(== (obj superClass?) Ego)
										)
										(obj illegalBits?)
									else
										-1
									)
								)
								#title (obj name?)
								#icon (obj view?) (obj loop?) (obj cel?)
							)
							(= i (cast next: i))
						)
					)
					(`@i
						(= i (GetNumber {ID number of the object?}))
						(ego get: i)
					)
					(`@k
						(EgoDead "It's all over for now. Please try again."
							#title {You're dead.}
							#icon vStaticIcons
						)
					)
					(`@m
						(theGame showMem:)
					)
					(`@e
						(Format @str "ego\nx:%d y:%d\nloop:%d\ncel:%d"
							(ego x?) (ego y?) (ego loop?) (ego cel?)
						)
						(Print @str
							#title {Ego}
							#icon (ego view?) 0 0
						)
					)
					(`@v
						(Show VMAP)
					)
					(`@p
						(Show PMAP)
					)
					(`@c
						(Show CMAP)
					)
					(`@d
						;Added to allow the interpreter's internal debugger
						;to be accessed with a simpler key combo
						(SetDebug)
					)
					(`@w
						(CreateObject doit:)
					)
					(else
						(event claimed: FALSE)
					)
				)
			)
		)
	)
)
