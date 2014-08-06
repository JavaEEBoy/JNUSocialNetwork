/**
 * 
 */

$(document)
		.ready(
				function() {
					window.onload = function() {
						var ID = $("#security-code-user-ID").val();
						/*
						 * initialize user info
						 */
						$.ajax({
							type : "GET",
							url : '../../JNUSocialNetwork/app/user/fetchByID/'
									+ ID,
							success : function(data) {
								sessionStorage.setItem("user", JSON
										.stringify(data));
								sessionStorage.setItem("onlineUserIDs", JSON
										.stringify([]));
							}
						});

						/*
						 * initialize websocket
						 */
						window.ws = new WebSocket(
								"ws://localhost:8080/JNUSocialNetwork/endpoint/connect/"
										+ ID);
						ws.onopen = function(e) {

						};

						ws.onclose = function(e) {

						};

						ws.onerror = function(e) {
						};

						ws.onmessage = function(e) {
							if ($.parseJSON(e.data).action != null) {
								switch ($.parseJSON(e.data).action) {
								case "CHAT":
									handle_message($.parseJSON(e.data));
									break;
								case "UPDATEMESSAGESTATUS":
									handle_update_message_status($
											.parseJSON(e.data));
									break;
								case "REMIND":
									handle_remind($.parseJSON(e.data));
									break;
								case "SENDCONTACT":
									handle_contact($.parseJSON(e.data));
									break;
								}
							} else if ($.parseJSON(e.data).name != null) {
								switch ($.parseJSON(e.data).name) {
								case "CONNECT":
									handle_connect($.parseJSON(e.data));
									break;
								case "DISCONNECT":
									handle_disconnect($.parseJSON(e.data));
									break;
								}
							}
						};

						function handle_message(data) {
							do_receive(data);
						}

						function handle_update_message_status(data) {
							do_change_status(data);
						}

						function handle_remind(data) {
							handle_unread_messages(data.unreadMessages);
							// handle_unhandledEvents(data.unhandledEvents);
						}

						function handle_contact(data) {
							data = data.contacts;
							data.sort(function(a, b) {
								-(a.online - b.online);
							});
							sessionStorage.setItem("chatrooms", JSON
									.stringify(data));
						}

						function handle_connect(data) {
							data = $.parseJSON(data.data);
							var chatRooms = sessionStorage.getItem("chatrooms");
							chatRooms = $.parseJSON(chatRooms);
							for (var i = 0; i < chatRooms.length; i++)
								if (chatRooms[i].m1ID == data.ID
										|| chatRooms[i].m2ID == data.ID)
									chatRooms[i].online = true;
							sessionStorage.setItem("chatrooms", JSON
									.stringify(chatRooms));
							$("#contact-list #" + data.ID + " span")
									.replaceWith(
											'<span class="glyphicon glyphicon-stop" style="color: rgb(45, 189, 48);"></span>');
							var IDs = sessionStorage.getItem("onlineUserIDs");
							IDs = $.parseJSON(IDs);
							IDs[IDs.length] = data.ID;
							sessionStorage.setItem("onlineUserIDs", JSON
									.stringify(IDs));
						}

						function handle_disconnect(data) {
							data = $.parseJSON(data.data);
							var chatRooms = sessionStorage.getItem("chatrooms");
							chatRooms = $.parseJSON(chatRooms);
							for (var i = 0; i < chatRooms.length; i++)
								if (chatRooms[i].m1ID == data.ID
										|| chatRooms[i].m2ID == data.ID)
									chatRooms[i].online = false;
							sessionStorage.setItem("chatrooms", JSON
									.stringify(chatRooms));
							$("#contact-list #" + data.ID + " span")
									.replaceWith(
											'<span class="glyphicon glyphicon-stop" style="color: rgb(169, 169, 169);"></span>');
						}

						/*
						 * initialize friend-list
						 */
						$("#contact-list a.open_button")
								.click(
										function() {
											var time = 300;
											var cssClass = $(
													"#contact-list .open_button span")
													.attr("class");
											if (cssClass
													.indexOf("glyphicon-chevron-left") != -1) {
												$("#contact-list .panel-footer")
														.css("right", 0);
												$("#contact-list").animate({
													right : "+=200px"
												}, time);
												$("#chatroom").animate({
													right : "+=200px"
												}, time);
												$(
														"#contact-list .open_button span")
														.replaceWith(
																'<span class="glyphicon glyphicon-chevron-right open_button"></span>');
											} else {
												$("#contact-list").animate({
													right : "-=200px"
												}, time);
												$("#chatroom").animate({
													right : "-=200px"
												}, time);
												$(
														"#contact-list .open_button span")
														.replaceWith(
																'<span class="glyphicon glyphicon-chevron-left open_button"></span>');
												$("#contact-list .panel-footer")
														.animate({
															right : '40px'
														}, time + 200);
											}
										});
						$("#contact-list .friends-tree-node-button").click(
								function() {
									on_friends_node_click();
								});
						$("#contact-list .remind-tree-node-button").click(
								function() {
									on_remind_node_click();
								});

					};

				});
