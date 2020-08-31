export var en: any = {
    "HOME": "Home",
    "EMAIL-GENERAL": {
        "ACTIONS": {
            "SAVE": "Save",
            "CANCEL": "Cancel",
            "OK": "OK",
            "DELETE": "Delete",
            "CONFIRM": "Confirm",
            "SELECT": "Select",
            "PAUSE": "Pause",
            "CREATE": "Create",
            "BACK": "Back",
            "ACTIONS": "Actions",
            "DE-LINK": "De-link",
            "OPEN-DETAILS": "Open Details",
            "ADD": "Add",
            "EXECUTE":"Execute",
            "MAP":"Merge field and Datasource mapping"
        },
        "ERRORS": {
            "REQUIRED": "Field is required or is Invalid.",
            "LENGTH-EXCEEDING": "Length cannot be greater than {{length}}.",
            "LENGTH-SHORT": "Length cannot be less than {{length}}.",
            "INVALID-FORMAT": "Invalid format.",
            "UPDATE-FAILED": "Error Occured while updating",
            "CREATE-FAILED": "Error Occured while creating"

        }
    },
    "COMPONENTS": {
        "STRUCTURE": {
            "REMOVE-BLOCK-MESSAGE": "Are you sure?",
            "CHANGE-BLOCK-SETTINGS": "Change block settings",
            "CHANGE-BLOCK-ORDER": "Change block order",
            "DUPLICATE-BLOCK": "Duplicate block",
            "DELETE-BLOCK": "Delete block"
        },
        "DIALOG": {
            "YES": "Yes",
            "NO": "No"
        },
        "PREVIEW": {
            "DESKTOP": "Desktop / 100%",
            "TABLET": "Tablet / 768px",
            "SMARTPHONE": "Smartphone / 360px",
        }
    },
    "ELEMENTS":{
        "BUTTON": {
            "DEFAULT-TEXT": "Click on me"
        },
        "TEXT-ELEMENT": {
            "MERGE-FIELD-BUTTON": "Merge Fields"
        },
        "EMPTY-BLOCK": {
            "TITLE": "Drag some blocks here."
        }
    },
    "EMAIL-EDITOR": {
        "TITLE": "Template Detail",
        "INFORMATION": "Information",
        "MERGE-FIELDS": "Merge Fields",
        "EMAIL-VARIABLE": {
            "TITLE": "Email Merge Fields",
            "ADD-TITLE": "Add Merge Field",
            "DEFAULT-VALUE-PLACEHOLDER": "Default Value",
            "FIELDS": {
                "PROPERTY-NAME": "Property Name",
                "PROPERTY-TYPE": "Property Type",
                "DEFAULT-VALUE": "Default Value",
                "LAST-MODIFICATION-TIME": "Modified Date",
                "LAST-MODIFIER-USER-ID": "Modified User",
                "SELECT-VALUE":"Select Date Format",
                "SELECT-DATE":"Select Date",
                "SELECT-DECIMAL":"Select Decimal Precision",
                "ADD-NEW":"Add New"
            },
            "TYPES": {
                "NUMBER":{
                    "INTEGER": "Integer",
                    "DECIMAL": "Decimal"
                },
                "LIST": {
                    "COMMA-SEPARATED": "Comma Seperated",
                    "BULLET-VERTICAL": "Bullet Verticle List",
                    "NUMBERED-VERTICAL": "Numbered Vertical List"
                },
                "IMAGE-DROPDOWN": {
                    "HORIZONTAL": "Horizontal",
                    "VERTICAL": "Vertical",
                }
            },
            "MERGE-VALUE":{
                "DATE":{
                    "LABEL": "Select Date Format"
                },
                "CURRENCY":{
                    "LABEL": "Select Currency Type",
                    "PLACEHOLDER": "Enter Currency"
                },
                "NUMBER":{
                    "LABEL": "Select Number Type"
                },
                "STORAGE":{
                    "LABEL": "Select Storage Type"
                },
                "POSITION":{
                    "LABEL": "Enter Position"
                },
                "LINK":{
                    "PLACEHOLDER": "Enter Link",
                },
                "LIST":{
                    "LABEL": "Select Storage Type"
                },
                "CLICKABLE-IMAGE":{
                    "LABEL": "Select Storage Type"
                },
            },
            "MESSAGES": {
                "NO-ASSOCIATION-SELECTED": "No {{table}} selected",
                "SELECT-ASSOCIATION": "Select {{table}}"
            },
            "ERRORS": {
                "DELETE-WITH-TEMPLATE": "'Merge field {{propertyName}} is used in email template(s) {{templates}}. Please remove this Merge field from {{templates}} to delete it.",
                "FILE-SIZE-EXCEEDING": "File size should be less than 2 MB",
                "NO-IMAGE": "Please enter image"
            }
        },
        "DATA-SOURCE":{
            "TITLE": "DataSource",
            "ADD-TITLE": "Add DataSource",
            "IMAGE": "image",
            "PREVIEW-TABLE": "Preview Table",
            "ERRORS":{
                "ALREADY-MAPPED": "This email template has already mapped with {{fields}}",
                "NO-QUERY": "Please enter any query",
                "DELETE-WITH-FIELDS": "This datasource is mapped with {{fields}} columns.Please remove mappings to delete this datasource.",
                "DELETE": "Datasource is already binded, can not delete",
                "NO-COMBINATION": "No combination exist"
            },
            "LABELS": {
                "DATASOURCE-BINDED": "This datasource is binded",
                "SQL-PLACEHOLDER": "Enter a valid Select SQL Query"
            },
            "FIELDS":{
                "NAME":"Name",
                "SQL-QUERY":"Sql Query",
                "EMAIL-TEMPLATE":"Email Template",
                "CREATION":"Created Date",
                "COLUMN-NAME":"Column Name",
                "DATA-TYPE":"Data Type"
            },
            "MERGE-MAP": {
                "TOTAL-MERGE-FIELD": "Total Merge Fields",
                "MAPPED-MERGE-FIELD": "Mapped Merge Fields",
                "NOT-AVAILABLE": "Not Available",
                "BLANK": "",
                "SAVE-MAP": "Save Map",
                "TITLE": "Merge Field & DataSource Mapping",
                "MAPPED-SUCCESSFULLY": "Data Source mapped successfully.",
                "FIELDS": {
                    "MERGE-FIELD": "Merge Field",
                    "DATASOURCE-FIELD": "Available Datasource Fields",
                    "MAPPING": "Mapping",
                }
            }
        },
        "EMAIL-TEMPLATE": {
            "ADD-TITLE": "Create Email Template",
            "TITLE": "Email Templates",
            "FIELDS": {
                "TEMPLATE-NAME": "Template Name",
                "DESCRIPTION": "Description",
                "SUBJECT": "Subject",
                "CATEGORY": "Category",
                "CC": "Cc",
                "BCC": "Bcc",
                "TO": "To",
                "ATTACHMENT-PATH": "Attachment Path",
                "ACTIVE": "Active",
                "CONTENT-HTML": "Content HTML",
                "CONTENT-JSON": "Content Json",
                "CREATION-TIME": "Created Date",
                "CREATOR-USER-ID": "Created User",
                "LAST-MODIFICATION-TIME": "Modified Date",
                "LAST-MODIFIER-USER-ID": "Modified User"
            }
        },
        "SAVE-EMAIL-BUTTON-TEXT": "Save & Continue",
        "SEND-TEST-EMAIL-BUTTON-TEXT": "Send a test email",
        "RESET-BUTTON-TEXT": "Reset",
        "TEMPLATE-ATTRIBUTE-TITLE": "Edit Email Template Attributes",
        "MESSAGES": {
            "SELECT-RECEIVER-PROMPT": "Where to send?",
            "EMAIL-SENT-SUCCESS": "Your test email has been successfully sent.",
             "EMAIL-SAVED-SUCCESS": "Email template saved.",
             "EMAIL-SENDING": "Sending email ....",
            "RESET-SUCCESS":"Email Template reset successfully",
            "SAMPLE-TEMPLATE": "<p>Dear Email Editor,</p><p >  With this email template editor, you can edit your text, and add buttons,images and dividers.The above logo image and dividers are for demo purpose, you can remove or replace them. You can view the list of supported elements by clicking the Drag & Drop Content section of the page</p><p>Thanks,</p><p>John</p>",
            "SAMPLE-TEMPLATE2": "It looks like this!",
            "NO-CHANGES": "There's no changes to be saved."
        },
        "ERRORS": {
            "TEMPLATE-MAPPED": "This email template is mapped with {{fields}} merge field(s)."
        }
    },
    "GROUPS": {
        "ALIGN": {
            "LABEL": "Align",
            "FIELDS": {
                "ALIGN": "Align"
            }
        },
        "BACK-REPEAT": {
            "LABEL": "Repeat",
            "FIELDS": {
                "BACKGROUND-REPEAT": "Background Repeat",
            },
            "LABELS": {
                "NO-REPEAT": "No Repeat",
                "REPEAT": "Repeat",
                "REPEAT-X": "Repeat X",
                "REPEAT-Y": "Repeat Y",
                "ROUND": "Round",
                "SPACE": "Space"
            }
        },
        "BORDER": {
            "LABEL": "",
            "FIELDS": {
                "BORDER-WIDTH": "Border Width",
                "STYLE": "Style",
                "RADIUS": "Radius"
            },
            "STYLES": {
                "SOLID": "Solid",
                "DASHED": 'Dashed',
                "DOTTED": 'Dotted'
            }
        },
        "COLOR": {
            "LABEL": "",
            "FIELDS": {
                "COLOR": "Color",
            }
        },
        "DIRECTION": {
            "LABEL": "Direction",
            "FIELDS": {
                "DIRECTION": "Direction"
            },
            "LABELS": {
                "LTR": "Left to Right",
                "RTL": "Right to Left"
            }
        },
        "FONT-STYLES": {
            "LABEL": "",
            "FIELDS": {
                "WEIGHT": "Weight",
                "SIZE": "Size",
                "FAMILY": "Family",
                "STYLE": "Style"
            },
            "STYLE-OPTIONS": {
                "ITALIC": "italic",
                "NORMAL": "normal",
                "OBLIQUE": "oblique"
            }
        },
        "LINE-HEIGHT": {
            "LABEL": "",
            "FIELDS": {
                "LINE-HEIGHT": "Line Height",
                "UNIT": "Unit"
            },
            "UNITS": {
                "PERCENTS": "Percents",
                "PIXELS": "Pixels",
                "NONE": "None"
            }
        },
        "LINK": {
            "LABEL": "Link",
            "FIELDS": {
                "LINK": "Link",
                "TARGET": "Target"
            },
            "LABELS": {
                "BLANK": "Blank",
                "SELF": "Self",
                "PARENT": "Parent",
                "TOP": "Top",
            }
        },
        "PADDING": {
            "LABEL": "",
            "FIELDS": {
                "TOP": "Top",
                "RIGHT": "Right",
                "BOTTOM": "Bottom",
                "LEFT": "Left",
            },
            "SIZE-PLACEHOLDER": "Size"
        },
        "WIDTH-HEIGHT": {
            "LABEL": "",
            "FIELDS": {
                "UNIT": "Unit",
                "LABEL": "Label",
                "AUTO": "Auto"
            },
            "UNITS": {
                "PERCENTS": "Percents",
                "PIXELS": "Pixels",
                "CONTAIN": "Contain",
                "COVER": "Cover",
            }
        },
    },
    "EMAIL-BUILDER": {
        "PREVIEW-BUTTON-TEXT": "Preview",
        "CLOSE-PREVIEW-BUTTON-TEXT": "Close Preview",
        "MESSAGES": {
            "NO-CHANGES": "There's no changes to be saved.",
            "UNSAVED-CHANGES-WARNING": "Please save unsaved changes to preview template.",
            "DIALOG-TITLE": "Are you sure?",
            "RESET-TITLE":"Are you sure, you want to reset the template?"
        },
        "STRUCTURE": {
            "CHANGE-SETTING-TOOLTIP": "Change structure settings",
            "CHANGE-SETTING-ARIA-LABEL": "Move structure",
            "CHANGE-ORDER-TOOLTIP": "Change structure order",
            "CHANGE-ORDER-ARIA-LABEL": "Move structure",
            "DUPLICATE-TOOLTIP": "Duplicate structure",
            "DUPLICATE-ARIA-LABEL": "Duplicate structure",
            "REMOVE-TOOLTIP": "Delete structure",
            "REMOVE-ARIA-LABEL": "Remove Structure"
        },
        "CONTENT": {
            "LABEL": "Content",
            "DRAG-AND-DROP-SECTIONS": {
                "LABEL": "Drag & Drop Sections",
                "BACK-TO-STRUCTURE-BUTTON-TEXT": "Back to structure blocks",
                "BACK-TO-STRUCTURE-BUTTON-ARIA-LABEL": "Back to blocks list",
                "BACKGROUND-IMAGE-LABEL": "Background Image",
                "BACKGROUND-COLOR-LABEL": "Background Color",
                "BACKGROUND-SIZE-LABEL": "Background Size",
            },
            "VARIABLES-AND-TAGS": {
                "LABEL": "Variables and Tags",
                "VARIABLE-NAME-LABEL": "Variable Name",
                "OPTIONS": {
                    "OPTION1": "Option 1",
                    "OPTION2": "Option 2",
                    "OPTION3": "Option 3",
                    "NONE": "None"
                },
                "DEFAULT-VALUE": "Default Value"
            },
            "DRAG-AND-DROP-CONTENT": {
                "LABEL": "Drag & Drop Content",
                "BACK-TO-CONTENT-BUTTON-TEXT": "Back to content blocks",
                "BACK-TO-CONTENT-BUTTON-ARIA-LABEL": "Back to blocks list",
                "TEXT": {
                    "COLOR-LABEL": "Color"
                },
                "IMAGE": {
                    "LABEL": "Image",
                    "TITLE-LABEL": "Title / Alt",
                    "WIDTH-LABEL": "Width",
                    "HEIGHT-LABEL": "Height",
                },
                "BUTTON": {
                    "LABEL": "Button text",
                    "COLOR-LABEL": "Color",
                    "BACKGROUND-LABEL": "Background",
                    "MARGIN-LABEL": "Margin",
                    "PADDING-LABEL": "Padding",
                },
                "SPACER": {
                    "HEIGHT-LABEL": "Height"
                },
                "SOCIAL": {
                    "MESSAGE": "Unfortunately this block is not ready yet!"
                }
            }
        },
        "SETTINGS": {
            "LABEL": "Settings",
            "WIDTH-LABEL": "Email Width",
            "BACKGROUND-COLOR-LABEL": "Background Color",
            "GLOBAL-PADDING-LABEL": "Global Padding",
        }
    }
}
