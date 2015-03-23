(ns damp.ekeko.EMF.reification  
  (:refer-clojure :exclude [== type])
  (:use [clojure.core.logic])
  (:use [damp.ekeko.logic])
  (:use [damp.ekeko.EMF.projectmodel])
  (:import [org.eclipse.emf.ecore EClass EReference]
           [java.util Collection])
)


(defn eobject [?eobject]
  (fresh [?p]
         (contains (emf-project-models) ?p)
         (project [?p]
                  (contains (.getEObjects ?p) ?eobject))))
(defn eobject-eclass [?eobject ?eclass]
  (all
    (eobject ?eobject)
    (equals ?eclass (.eClass ?eobject))))

(defn eclass-efeature 
  [?ec ?ef]
  (all 
    (v+ ?ec)
    (succeeds (instance? EClass ?ec))
    (project [?ec]
             (contains (.getEAllStructuralFeatures ?ec) ?ef))))

(defn ehas 
  [?eo ?feature-name ?val]
  (fresh [?ec ?ef ?feature-nameS]
         (eobject-eclass ?eo ?ec)
         (eclass-efeature ?ec ?ef)
         (equals ?feature-nameS (.getName ?ef))
         (equals ?feature-name (keyword ?feature-nameS))
         (equals ?val (.eGet ?eo ?ef))))

(defn eref 
  [?eo ?eref ?val]
  (fresh [?ec ?ef ?ef-name]
         (eobject-eclass ?eo ?ec)
         (eclass-efeature ?ec ?ef)
         (succeeds (instance? EReference ?ef))
         (equals ?ef-name (.getName ?ef))
         (equals ?eref (keyword ?ef-name))
         (equals ?val (.eGet ?eo ?ef))))

(defn echild
  [?eo ?name ?val]
  (fresh [?list]
         (eref ?eo ?name ?list)
         (conde [(succeeds (instance? Collection ?list))
                  (contains ?list ?val)]
                [(equals ?val ?list)]
        )))

(defn echild+
  [?eo ?name ?val]
  (fresh [?ch]
         (echild ?eo ?name ?ch)
         (conde [(== ?ch ?val)]
                [(echild+ ?ch ?name ?val)])))

(defn etype
  [?etype-name ?eobject]
  (fresh [?etype]
    (eobject-eclass ?eobject ?etype)
    (equals ?etype-name (keyword (.getName ?etype)))))
 