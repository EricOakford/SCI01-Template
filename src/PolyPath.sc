;;; Sierra Script 1.0 - (do not remove this comment)
(script# POLYPATH)

;; Path around an arbitrary set of obstacles, all of which are
;; defined as Polygons and added to the obstacle list via the
;; Rooms setObstacle method. 07/24/90 J.M.H.

(include system.sh) (include sci2.sh)
(use Main)
(use Motion)

(public
	PolyPath 0
)

(procedure (PolyPath param1 param2)
	(return
		(|
			(StrAt param1 (* 2 param2))
			(<< (StrAt param1 (+ 1 (* 2 param2))) $0008)
		)
	)
)

(class PolyPath of Motion
	(properties
		client 0
		caller 0
		x 0
		y 0
		dx 0
		dy 0
		b-moveCnt 0
		b-i1 0
		b-i2 0
		b-di 0
		b-xAxis 0
		b-incr 0
		completed 0
		xLast 0
		yLast 0
		value 2  ; current location in path
		points 0  ; pointer to path array allocated in the kernel

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
		(if (== (PolyPath points value) $7777)
			(super moveDone:)
		else
			(self init:)
		)
	)
	
	(method (setTarget)
		(if (!= (PolyPath points value) $7777)
			(= x (PolyPath points value))
			(= y (PolyPath points (++ value)))
			(++ value)
		)
	)
)
