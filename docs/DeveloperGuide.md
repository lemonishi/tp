---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* small delivery startup owners (e.g., subscription box services) in Singapore
* have limited manpower for admin work
* have limited road experience and are unfamiliar with local addresses
* can type fast and prefer typing over mouse interactions
* are comfortable using CLI apps

**Value proposition** (Client2Door):

* organizes customer contact and delivery details in one place
* provides a CLI-based alternative to GUI spreadsheets
* enables faster delivery tracking via quick access to client details
* helps optimize delivery routes by clustering addresses


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                 | So that I can…​                                                        |
|----------|--------------------------------------------|------------------------------|------------------------------------------------------------------------|
| `* * *`  | unfamiliar user                                   | see usage instructions       | refer to instructions when I forget how to use Client2Door             |
| `* * *`  | unfamiliar user                              | access a help page of commands (user guide) | know what each command does and how to use them        |
| `* * *`  | small business owner                       | add a new customer with contact details, address, and subscription status | manage deliveries and customer communication |
| `* * *`  | small business owner                       | view current subscribers for the month | plan monthly deliveries efficiently                             |
| `* * *`  | small business owner                       | search customers by name/phone/address keyword | find  details quickly during calls or delivery attempts    |
| `* * *`  | small business owner                       | view a customer's delivery address and details | deliver orders accurately                                     |
| `* * *`  | driver with limited road experience        | open directions (Google Maps link) to my next location | reach the next stop efficiently                    |
| `* * *`  | small business owner                       | check off customers who have received their monthly box | track completed deliveries for the month              |
| `* * *`  | small business owner                       | delete customers with expired subscriptions | keep the customer list updated each month                      |
| `* * *`  | CLI-lover                                  | use keyboard commands as much as possible | reduce time wasted navigating with a mouse                         |
| `* *`    | new business owner                         | access the app with a password | protect client confidentiality and trust                              |
| `* *`    | small business owner                       | edit a customer delivery entry    | correct mistakes and handle last-minute changes                  |
| `* *`    | small business owner                      | add remarks to a delivery (e.g. reason for failed delivery) | avoid repeating the same mistakes |
| `* *`    | small business owner                       | import and export customers and order details | avoid retyping existing data                                |
| `* *`    | small business owner                       | group customers staying in the same block/estate/area | complete all deliveries in that area without revisiting |
| `* *`    | small business owner who prefers commands over a graphical interface | generate a route grouped by location using a single command | minimize repeated trips easily |
| `* *`    | small business owner                       | highlight blatantly erroneous entries | reduce administrative workload (data checking)                    |
| `*`      | small business owner                       | hide private customer details | minimize chance of someone else seeing them by accident                |
| `*`      | long-time user                             | delete specific addresses (subscribers) | keep subscribers viewable while removing non-subscribers        |
| `*`      | long-time user                             | view a decent-looking UI     | make everyday usage less mundane while keeping information viewable    |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `Client2Door` and the **Actor** is the `Startup Owner`, unless specified otherwise)

---

**Use case: UC01 — Add a subscriber**

**MSS**

1.  Startup owner requests to add a subscriber.
2.  Client2Door adds the subscriber to the active subscriber list.
3.  Client2Door displays a success message and updated subscriber list.

    Use case ends.

**Extensions**

* 1a. Startup owner enters an invalid command word (e.g., typo, misspelling).

    * 1a1. Client2Door shows an error message indicating the command is invalid.

      Use case ends.

* 1b. Startup owner enters an invalid command format (e.g., parameters issue).

    * 1b1. Client2Door shows an error message indicating the format is invalid.

      Use case ends.

* 1c. Startup owner enters an invalid phone number (e.g., incorrect number of digits).

    * 1c1. Client2Door shows invalid phone number error message.

      Use case ends.

* 1d. Startup owner enters an invalid email address.

    * 1d1. Client2Door shows invalid email error message.

      Use case ends.

* 1e. Startup owner enters an invalid postal code.

    * 1e1. Client2Door shows invalid postal code error message.

      Use case ends.

* 1f. Startup owner adds a duplicate subscriber.

    * 1f1. Client2Door shows duplicate subscriber error message.

      Use case ends.

---

**Use case: UC02 — Delete a subscriber**

**MSS**

1.  Startup owner requests to delete a subscriber at specific index in list.
2.  Client2Door deletes the specified subscriber.
3.  Client2Door displays success message and the updated subscriber list.

    Use case ends.

**Extensions**

* 1a. Startup owner enters an invalid index.

    * 1a1. Client2Door shows invalid index error message.

      Use case ends.

* 1b. Startup owner enters an invalid command format (e.g., parameters issue).

    * 1b1. Client2Door shows an error message indicating the format is invalid.

      Use case ends.

---

**Use case: UC03 — List subscribers**

**MSS**

1.  Startup owner requests to list all active subscribers.
2.  Client2Door displays all subscribers and all stored details.

    Use case ends.

**Extensions**

* 1a. Startup owner enters an invalid command word (e.g., typo, misspelling).

    * 1a1. Client2Door shows an error message indicating the command is invalid.

      Use case ends.

* 1b. Startup owner enters an invalid command format (e.g., parameters issue).

    * 1b1. Client2Door shows an error message indicating the format is invalid.

      Use case ends.

* 2a. The subscriber list is empty.

   * 2a1. Client2Door displays no subscribers
        
     Use case ends.

### Non-Functional Requirements

**Environment Requirements**
* The system should work on any _mainstream OS_ with Java `17` or above installed. 
* The system should work without requiring an installer. 
* The system should not depend on any remote server maintained by the development team.

**Performance & Capacity Requirements**
* The system should manage up to 1000 persons without noticeable performance degradation. 
* The system should respond to user commands within 1 second when managing up to 1000 persons.

**Usability & Accessibility Requirements**
* Users should be able to complete most tasks faster using commands than using a mouse. 
* New users should be able to perform basic commands after reading the User Guide without external assistance. 
* The GUI should display correctly without layout distortion at 1920×1080 (scaling 100 - 125%), and remain fully functional at 1280×720 (scaling up to 150%).

**Data Requirements**
* The system should support persistent local storage of user data. 
* The system should restore previously saved data upon restart. 
* The system should prevent corruption of stored data during unexpected shutdown. 
* The system should save data automatically after each modifying command.

**Reliability & Error Handling**
* The system should operate continuously without failure during normal usage sessions. 
* Invalid commands should not cause the system to crash. 
* The system should display informative error messages for invalid user inputs. 
* The system should handle unexpected or malformed input gracefully.

**Maintainability & Extensibility**
* The codebase should follow standard Java coding conventions.
* The system should be modular to allow future feature extensions.

**Security & Privacy**
* User data should be stored locally and not transmitted externally without user action.

**Documentation**
* A User Guide describing all commands should be provided.
* A Developer Guide describing the system architecture should be provided to facilitate maintenance.

### Glossary

* **CLI (Command Line Interface)**: A text-based interface where users interact with the system by typing commands instead of using graphical controls.

* **Subscriber**: A customer with an active subscription who is expected to receive  recurring deliveries.

* **Subscription status**: The state indicating whether a customer currently has an active subscription.

* **Delivery status**: The outcome of a delivery attempt (e.g., succeeded or failed).

* **Startup owner**: The primary user of the system, typically a small business owner managing customer subscriptions and deliveries.

* **Delivery driver**: A user responsible for carrying out deliveries using the information stored in the system.

* **Mainstream OS**: A widely used operating system such as Windows, macOS, or Linux.

* **Private contact detail**: Subscriber information that should only be accessible by the owner (e.g., phone numbers or addresses).

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
