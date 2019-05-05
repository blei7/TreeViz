#
# This is a Shiny web application. You can run the application by clicking
# the 'Run App' button above.
#
# Find out more about building applications with Shiny here:
#
#    http://shiny.rstudio.com/
#

library(shiny)
library(tidyverse)
library(DT)
library(plotly)
library(leaflet)
library(ggmap)



#load data
fire_df  <- read_csv("../data/H_FIRE_PNT.csv") %>% 
    mutate(city = as.factor(city))

ui <- fluidPage(
        titlePanel("Visualizing Crimes in US"),
        
        #Side panel frame
        sidebarLayout(
          sidebarPanel(
            style = "position:fixed;width:fixed;", ##fix sidebar pos, but overlap in narrow size broswer
            

            #Input: select years
            sliderInput("year", 
                        label ="Year",
                        min = 1975, max = 2015,
                        value = c(1975,2015),
                        step = 1, sep = "")

          ),
          
          
          #Main panel frame with 3 tabs
          mainPanel(
            tabsetPanel(type = "tabs",
              tabPanel("Plot",
                       leafletOutput("map")),
              tabPanel("Economic", span("Data source:",
                                    tags$a(href = "https://github.com/themarshallproject/city-crime", "The Marshall Project")), dataTableOutput("table")),
              tabPanel("About", 
                       h2("Description"),
                       br(), 
                       "This web app allows for visual comparision of the impact of forest fires.The app is design to help better educate kids on forest fires and climate change in general.",
                       br(), 
                       br(),
                       br(),
                       "Contributors: Bailey Lei, Alex Pak, Sabrina Tse, June Wu",
                       br(),
                       span("Github Repository:",
                            tags$a(href = "https://github.com/UBC-MDS/DSCI_532_Crime_Blei7_simchi", "Visualizing crimes in US")),
                       br(),
                       "Please contact Bailey Lei (baileylei@gmail.com) with questions, comments or concerns.")
                      

            )
          )
        )
)

server <- function(input, output) {
  
  #filter data frame based on user input
  filtered <- reactive({
    crime_df %>%
      filter(year >= input$year[1],
             year <= input$year[2],
             city %in% input$city,
             category == input$crime)
      
     
  } )
  
  #Time series plot of crime rates 
  output$TimeSeries <- renderPlotly({
    filtered() %>% 
        ggplot(aes(year, crime_rate)) +
        geom_line(aes(colour=city, group=category))+
        xlab("") +
        ylab("cases per 100,000 people") + 
        ggtitle(paste("Time Series of", input$crime, "Cases in U.S. from", input$year[1], "to", input$year[2]))
  })
  
  #Boxplot of crime rates  
  output$boxplot <- renderPlotly({hide_legend(
    filtered() %>% 
      ggplot(aes(city, crime_rate)) +
      geom_boxplot(aes(fill = city, alpha=0.7)) + 
      xlab("") +
      ylab("cases per 100,000 people") +  
      theme(axis.text.x = element_text(angle=45, hjust=1))+
      ggtitle(paste("Distribution of", input$crime, "cases from", input$year[1], "to", input$year[2])))
    
    
  })
  #generate US map with market base on input cities
  output$map <- renderLeaflet({
    leaflet() %>% setView(lng = -95.7129, lat = 37.0902, zoom = 3) %>% addTiles() %>% addMarkers(data  = geocode(input$city, source = "dsk"), label =input$city, )
  })
  
  
  #Dataset 
  output$table<-DT::renderDataTable(
    {
      DT::datatable(filtered(),options = list(lengthMenu = c(30,50,100),pageLength = 10))
    }
  )
  
}
shinyApp(ui = ui, server = server)