;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  RANDCYC.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1990
;;;;
;;;;  Written:
;;;;     Doug Oldfield
;;;;     July 25, 1990
;;;;
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     July 24, 1992
;;;;
;;;;  Cycles cels randomly & constantly
;;;;
;;;;  Classes:
;;;;    RandCycle


(script# RANDCYC)
(include system.sh) (include sci2.sh)
(use Main)
(use Motion)


(class RandCycle of Cycle
	(properties
		client 0
		caller 0
		cycleDir 1
		cycleCnt 0
		completed 0
		surrogate -1
	)
	
	(method (init obj theTime whoCares)
		(super init: obj)
		(if (>= argc 2)
			(= surrogate theTime)
			(if (>= argc 3) (= caller whoCares))
		else
			(= surrogate -1)
		)
	)
	
	(method (doit)
		(++ cycleCnt)
		(if (> cycleCnt (client cycleSpeed?))
			(if surrogate
				(if (> surrogate 0) (-- surrogate))
				(client cel: (self nextCel:))
				(= cycleCnt 0)
			else
				(self cycleDone:)
			)
		)
	)
	
	(method (nextCel &tmp newCel)
		(while
			(==
				(= newCel (Random 0 (client lastCel:)))
				(client cel?)
			)
		)
		(return newCel)
	)
	
	(method (cycleDone)
		(= completed TRUE)

		;If we have a caller which needs cue:ing, set the flag for a delayed cue:.
		;Otherwise, just cue: ourselves to complete the motion.
		(if caller (= doMotionCue TRUE)
			else
			(self motionCue:)
			)
	)
)
