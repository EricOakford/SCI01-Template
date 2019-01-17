;;; Sierra Script 1.0 - (do not remove this comment)
(script# COUNT)
(include system.sh) (include sci2.sh)

(public
	Count 0
)

(procedure (Count theList theCode &tmp theCount theNode)
	(= theNode (FirstNode (theList elements?)))
	(= theCount 0)
	(while theNode
		(if (theCode doit: (NodeValue theNode) &rest) (++ theCount))
		(= theNode (NextNode theNode))
	)
	(return theCount)
)
