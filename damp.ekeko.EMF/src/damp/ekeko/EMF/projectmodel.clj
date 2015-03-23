(ns ^{:doc "Access point for EMF documents within a project"
    :author "Carlos Noguera"}
  damp.ekeko.EMF.projectmodel
  (:require [damp.ekeko [ekekomodel :as ekekomodel]])
  (:import 
    [damp.ekeko.EMF.modelFactories EMFModel]
    ))

(defn 
  emf-project-models
  "Return all EMF project models to be queried"
  []
  (filter (fn [project-model] 
          (instance? EMFModel project-model))
          (ekekomodel/queried-project-models)))

