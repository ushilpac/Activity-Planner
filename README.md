# Pre-work - Activity Planner

Activity Planner is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Shilpa Chaudhary

Time spent: 12 hours 

## User Stories

The following required functionality is completed:

* [ ] User can successfully add and remove items from the todo list
* [ ] User can tap a todo item in the list and bring up an edit screen for the todo item and then have any changes to the text reflected in the todo list.
* [ ] User can persist todo items and retrieve them properly on app restart

The following optional features are implemented:

* [ ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [ ] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ ] Add support for completion Due Date for todo items (and display within listview item)
* [ ] Add support for selecting the priority of each todo item (and display in listview item)
* [ ] Tweak the style improving the UI / UX, play with colors, images or backgrounds


## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/D3geAit.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />



## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

   My experience working so far with android was really interesting. I worked first time with app development but with support of big android community didnt find difficult to undersatnd and 
   resolve the problems came across. I can not compare layout approach with others as do not have experience of developing on other platform.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

	Adapter is a bridge between data source and adapterview. Adapter configures 2 aspects which array to use as the data sourece of the list and how to convert any given item in the array into a corresponding view object.
	The adapetr are built to reuse the views, when a view is scrolled so that it is no longer visible,it can be used for one of the new view apperaing. This reused view is the convertview. If convertview is null it means
	that ther is no recycle view and we have to create new one,otherwise we should use it.

## Notes

	I worked on UI for the first time so it was a slower process for me and i had to put more efforts on understanding the layouts and its interaction 
	with each other, handling events etc.

## License

    Copyright [2017] [Shilpa Chaudhary]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.