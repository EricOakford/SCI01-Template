;;; Sierra Script 1.0 - (do not remove this comment)
(script# SORT)
(include game.sh)
(use System)

(public
	Sort 0
)

(procedure (Sort theList theLTCode &tmp temp node minValue tempList)
	
	;;Transfer sort (yeech!), isnt really bad since our lists are 
	;;only about 10 elements long. Conserving heap memory is a higher
	;;priority nowadays...
	;;
	;; --Pablo Ghenis 6/9/89

	(= tempList (List new:))
	(while (= node (FirstNode (theList elements?)))
		(= minValue (NodeValue node))
		(while node
			(if
			(theLTCode doit: (= temp (NodeValue node)) minValue &rest)
				(= minValue temp)
			)
			(= node (NextNode node))
		)
		(transferElement doit: minValue theList tempList)
	)
	(tempList
		eachElementDo: #perform transferElement tempList theList
		dispose:
	)
)

(instance transferElement of Code
	(properties name "TE")
	
	(method (doit e sourceList destinationList)
		(destinationList addToEnd: e)
		(sourceList delete: e)
	)
)
