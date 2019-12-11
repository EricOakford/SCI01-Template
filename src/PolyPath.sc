;;; Sierra Script 1.0 - (do not remove this comment)
(script# 945)
(include game.sh)
(use Main)
(use Motion)
(use System)

;EO: this is WordAt, which is already in SYSTEM.SC
;;;(public
;;;	proc945_0 0
;;;)
;;;
;;;(procedure (proc945_0 param1 param2)
;;;	(return
;;;		(|
;;;			(StrAt param1 (* 2 param2))
;;;			(<< (StrAt param1 (+ 1 (* 2 param2))) $0008)
;;;		)
;;;	)
;;;)

(class PolyPath of Motion
	(properties
		value 2
		points 0
	)
	
	(method (init actor theX theY whoCares opt)
		(if argc
			(= client actor)
			(if (> argc 1)
				(= points
					(AvoidPath
						(actor x?)
						(actor y?)
						theX
						theY
						(if (curRoom obstacles?)
							((curRoom obstacles?) elements?)
						else
							0
						)
						(if (curRoom obstacles?)
							((curRoom obstacles?) size?)
						else
							0
						)
						(if (>= argc 5) opt else 1)
					)
				)
				(if (> argc 3) (= caller whoCares))
			)
		)
		(self setTarget:)
		(super init:)
	)
	
	(method (dispose)
		(if points (Memory MDisposePtr points))
		(super dispose:)
	)
	
	(method (moveDone)
		(if (== (WordAt points value) 30583)
			(super moveDone:)
		else
			(self init:)
		)
	)
	
	(method (setTarget)
		(if (!= (WordAt points value) $7777)
			(= x (WordAt points value))
			(= y (WordAt points (++ value)))
			(++ value)
		)
	)
)
