(ns mtodo.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

;; -------------------------
;; Reducers

(defmulti todo-reducer (fn [state ty data] ty))
(defmethod todo-reducer :default [state ty data]
  (do
    (.error js/console "Redux: Unknown action type: " (str ty))
    state))

(defmethod todo-reducer :todo-reset [state ty data]
  (assoc state :todos (sorted-map)))

(defmethod todo-reducer :todo-add [state ty {:keys [title done]}]
  (let [state (update state :counter inc)
        id (:counter state)]
    (assoc-in state [:todos id] {:id id :title title :done done})))

(defmethod todo-reducer :todo-toggle [state ty {:keys [id]}]
  (update-in state [:todos id :done] not))

(defmethod todo-reducer :todo-edit [state ty {:keys [id]}]
  (assoc-in state [:todos id :editing] true))

(defmethod todo-reducer :todo-edit-save [state ty {:keys [id title]}]
  (assoc-in state [:todos id :title] title))

(defmethod todo-reducer :todo-edit-stop [state ty {:keys [id]}]
  (assoc-in state [:todos id :editing] false))

;; -------------------------
;; Data

(defonce state (atom {:counter 0
                      :todos (sorted-map)}))

(def reducers [todo-reducer])

(defn redux [state reducers ty data]
  (reduce #(%2 %1 ty data) state reducers))

(defn dispatch! [ty data]
  (swap! state redux reducers ty data))

(def init
  (do
    (dispatch! :todo-reset {})
    (dispatch! :todo-add {:title "Feed my cats"})
    (dispatch! :todo-add {:title "Do laundry" :done true})
    (dispatch! :todo-add {:title "Read a book"})))

;; -------------------------
;; Views

(defn inputbox [{:keys [text on-save on-stop]}]
  (let [val (atom text)
        stop #(do
                #_(reset! val "")
                (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
                (when on-save
                  (if-not (empty? v) (on-save v))
                  (stop)))]
    (fn [props]
      [:input (merge props
                     {:type :text :value @val :on-blur save
                      :on-change #(reset! val (-> % .-target .-value))
                      :on-key-down #(case (.-which %)
                                      13 (save)
                                      27 (stop)
                                      nil)})])))

(defn todolistitem [{:keys [id title done editing]}]
  (let [todo-id (str "todo_" id)]
    [:li
     [:input {:type :checkbox :checked done
              :id todo-id
              :on-change #(dispatch! :todo-toggle {:id id})}]
     (if editing
       [inputbox {:text title
                  :on-save #(dispatch! :todo-edit-save {:id id :title %})
                  :on-stop #(dispatch! :todo-edit-stop {:id id})}]
       [:label {:on-click #(dispatch! :todo-edit {:id id})} title])]))

(defn todolist [todos]
  [:ul
   (for [id (keys todos)]
     ^{:key id} [todolistitem (get todos id)])])

(defn home-page []
  [:div [:h2 "Welcome to mtodo"]
   [:div [:a {:href "/about"} "go to about page"]]
   [:div [:h3 "Your todos"]]
   [:div [todolist (:todos @state)]]])

(defn about-page []
  [:div [:h2 "About mtodo"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
                    (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
                    (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
