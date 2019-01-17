;;; Sierra Script 1.0 - (do not remove this comment)
(script# WANDER)
(include system.sh) (include sci2.sh)
(use Motion)


(class Wander of Motion
	;;; Wander about the screen.  This motion never terminates.

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
		distance 30	;the max distance to move on any one leg of the wander
	)
	
	(method (init theObj dist)
		(if (>= argc 1)
			(= client theObj)
			(if (>= argc 2) (= distance dist))
		)
		(self setTarget:)
		(super init: client)
	)
	
	(method (doit)
		(super doit:)
		(if (client isStopped:) (self moveDone:))
	)
	
	(method (moveDone)
		(self init:)
	)
	
	(method (setTarget &tmp diam)
		;Pick a random position to move to, constrained by 'distance'.
      (= x (+ (client x?) (- distance (Random 0 (= diam (* distance 2))))))
      (= y (+ (client y?) (- distance (Random 0 diam))))
   )
	
	(method (onTarget)
		(return FALSE)
	)
)
