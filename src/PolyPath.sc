;;; Sierra Script 1.0 - (do not remove this comment)
(script# POLYPATH)
(include game.sh)
(use Main)
(use Motion)

;; Path around an arbitrary set of obstacles, all of which are
;; defined as Polygons and added to the obstacle list via the
;; Rooms setObstacle method. 07/24/90 J.M.H.




(class PolyPath of Motion
	(properties
		value    2  ; current location in path
		points   0  ; pointer to path array allocated in the kernel
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
		(= points NULL)
		(super dispose:)
	)
	
	(method (moveDone)
		(if (== (WordAt points value) $7777)
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
