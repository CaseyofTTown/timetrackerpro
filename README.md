Time tracker pro is a java application built 
with swing for graphical user interface. 
A user is prompted to register, enter their 
username, password, and reset pin. Once registered
this app collects the users full name, their
ems cert if they have one, and its expiratiin
date. All of this is stored in a local sqlite 
database as well as tables for time sheets,
daily logs, and the associated ambulance calls.

Upon enetering the main page of the app the
user will see a greeting label, a clock that
updates every 15 seconds, and their cert information.
Below this is a tabbed pane which starts at 
the time sheet panel which has two 
date range pickers that automatically calculate
and show the last 30 days from today but can 
be updated by selecting new dates and pressing
update. This controlls what time sheets will 
be shown from the database.

The user can enter a time sheet using the button
or hotkey ctr + t. Once theyve entered a Time 
sheet they can either navigate to the log pane
to manually create a log. Or the user can select 
the time sheet and either press the orange button
that says quick log from time sheet, or they can
use the hotkey ctr + l. Once a log is entered
the user can add ambulance calls which are 
categorized by emergency, non emergency, 
transfer, or refusal. There is also a flag to
mark and track if the patient was skilled or
not. A log can be modified by selecting it. 
(the background will turn blue) and then 
pressing the modify or delete button in the 
tray below. Deleting a log that contains calls
will also delete those calls from the database
to maintain data integrity.

To add calls to a 
log, click expand and then press add call. 
This will trigger a custom interface to 
collect the data including a ticker for isSkilled()
This dialog and most other's are also hotkeyed.
Pressing enter will submit the data, escape will
cancel it. By now youve noticed most forms in
the application do some form validation, ie if
you select a refusal call, drop off address,
mileage, insurance are greyed out and disabled
because they won't be needed for that submission.

The delete and modify call buttons are 
independent of the delete and modify log 
buttons at the very bottom tray. They function
in the same manner as the logs where a call must
be selected to modify it or delete it. If you 
select a call to modify the same dialog that 
was used to create the call will be shown 
with the data preloaded. Verify the isSkilled
box before submission. The screen will have to 
refresh because of how the data for this class
is setup but when a user navigates back to this
screem or presses the update date range (even
if you dont change the range) that will 
refresh a counter on the log which shows how
many calls that log contains. Another trick
on the log panel is that the date pickers here 
have listeners so changing the start date or
the end date will automatically change the
display of what logs are shown. (Past 30
days is default for time sheets and logs).

Room was left for future expansion with the 
medication sign out pane so an image was 
added for now "under construction". The next 
pane the user will find is the manage employee 
status panel. This panel was added to allow
anyone to toggle the isActive; boolean flag 
that each employee has. This way if an 
employee quits, they will be excluded from 
the drop down menus and cert report but their
data is kept and they can always be set back
to active at any time. To move an employee from
say active to inactive you simply
click the employee's name, click deactivate,
and they will move from left panel to right,
their name going from Gold to crimson red.

The final panel of the application is used to
generate reports to be printed. The panel to 
the left is somewhat of a preview however I 
overrode the print function in order to 
prevent clipping and attempt pagination. 
There are 4 buttons.

Time sheet report will 
pop open a dialoge to collect two dates, a 
start and end date. If youre generating payroll
remember any date within the range will be 
included. (Our payday goes Tuesday - Tuesday 
but you should input Tuesday and then 
on the second week you should select Monday)
Someone who started a shift on monday would 
end their shift Tuesday morning when time 
sheets are collected. But if you input 
Tuesday - Tuesday, then whoever started their
shift that Tuesday morning of collection day
(should be on the next week...) would be 
included as well. At the bottom of the panel
there are two buttons. Print and Clear. 
Print will trigger a dialoge with print options
and here is where you can decide. If you 
choose microsoft to PDF, then youll be able to
print, fax, email, share on facebook messenger,
extra... If you try to straight print... Im 
hoping it will function well ':). Pressing 
clear resets the dialog.

The next option is the generate the log reports.
it will also ask for two date ranges. Be
careful not to select too many logs at once or
you may end up with weird printing/clipping 
issues. Multiple things can be printed together
like Logs and Counts for example but its best
to not go crazy with it and to clear the panel.
Logs will print with a table of the ambulance
calls that were ran that shift as well as if 
they were skilled or not. 

The next button generates a Certification table.
This is where that information that was collected
upon registering is used. Also notice the top bar.
if youre certified, it shows how many days until
your cert expires and offers a button to update
that information. If youre a driver it offers
to add a certification. The certification table
will only print out the employees who have 
isActive() = true; which is by default but 
thats what the manage employee status panel 
is for. 

The last button on this panel is used to 
collect two dates and it will make a call
to the database to get a list of all the
ambulance calls in that range. The function 
then counts the number of each type of call 
and how many were skilled and returns this
in a table for printing. The date range that
is used will be above the table, this way 
there's no confusion when coming back later 
to collect and print more data.

Unless this application is going to be
installed on a very reliable, robust system
I HIGHLY recommend printing records on a 
regular basis. I know for sure printing as 
a PDF file results in a nice formatted 
document, so i recommend doing that and then
printing that pdf. This data is all local
so if the application is destroyed or the pc
rendered in operable, the data is gone. 
With records printed on a regular basis, the
only other frustration would be users having to
recreate their accounts in such a scenario, 
however if properly installed in a protected
folder with a desktop shortcut, this issue 
csn be potentially avoided. 

Initially i was going to create a sign out
method however the only reason to sign out
and sign another user in would be if that 
user wanted to update their cert information.
In that case or to add new users, it's best 
to close and reoppen the application as this
provides a fresh start and is less likely to
lead to data inconsistency or null pointer 
errors. Any signed in user is able to create
modify and destroy entries of other users 
however as bad as that sounds our current
practice is exactly the same. I.E i take your
log out of the drawer and throw it in the 
paper shredder becaude you threw away my 
butter. This provides another reason why 
reports should be printed on a semi regular 
basis and if data inconsistency is noticed 
it should be investigated (missing date gap
in the time sheets for example). 
//code explanation 

This application follows Model - View - Controller
pattern and I implemented certain elements
different ways. For my first fiew classes I
psssed buttons and components up to the controller
and set the action listeners within the controller
to call the functions and trigger actions.

This was fine at first but as the application
grew it quickly started looking like a mess.
So with time being a factor and the fact this
was a voluntary project i decided to leave 
the original architecture in place for the first
for elements but going forward, I created a
private variable of my controller class within 
the first view class. View is created 
within controller so it cant be passed as 
a parameter but once view is created it can 
be set with a setter function 
'private void setController(TTController controller){
     this.controller = controller; } and 
     of course youd have private TTController controller;
at the top of the class. Then other panels 
can accept the TTController in their 
constructors as an argument and call this.controller = controller;
to set their instance variables as well.

This significantly simplified the architecture 
as now i could write a function in my controller
which has my Database manager class, to do 
some task and return a result directly without 
having to litter several classes by building 
chains of getters and setters.

There are some cuetom classes for listeners 
which handle things such as selecting and
deselecting the log cards. JDatePickerImpl
is the date picker i customized and use throughout 
the program. Feel free to message if you have 
any questions or want to know more. A lot 
of code was added in the brainstorming stage 
that is not currently in use but may very well
be implemented in future updates depending
on arrangements made.
