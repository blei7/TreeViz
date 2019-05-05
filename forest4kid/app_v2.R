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
library(lubridate)
library(leaflet.extras)

fire_df  <- read_csv("../data/H_FIRE_PNT.csv")
co_data  <- read_csv("../data/CO_2000_2018.csv") 

head(fire_df_heat)
fire_df_heat <- fire_df %>%
  filter(FIRE_YEAR >= 2000) %>%
  group_by(FIRE_YEAR, LONGITUDE, LATITUDE) %>%
  summarise(intensity = sum(SIZE_HA))

# co_data_day <- co_data %>%
#   mutate(date = date(date)) %>%
#   mutate(day = ymd(date)) %>%
#   group_by(day) %>%
#   summarise(mean(ppm))

co_data_year <- co_data %>%
  mutate(date = date(date)) %>%
  mutate(year = year(date)) %>%
  group_by(year) %>%
  summarise(avg = mean(ppm)) %>%
  mutate(avg = as.numeric(avg))

co_data_year$color <- cut(co_data_year$avg, 
                          breaks = c(0, 0.3, 0.4, 0.5, 1), 
                          labels = c("#45050c", "#720e07", "#8b6220", "#d5ac4e"))

head(co_data_year)

# head(co_data_year)
# 
# co_data_year %>%
#   ggplot(aes(x = year, y = avg)) +
#   geom_point()

# Define UI for application that draws a histogram
ui <- fluidPage(

    # Application title
    titlePanel("Tree Huggers"),

    # Sidebar with a slider input for number of bins 
        mainPanel(
          tabsetPanel(type = "tabs",
                  tabPanel("Fire Zone",
                           leafletOutput("map1"),
                           #Input: select years
                           sliderInput("year",
                                       label ="Year",
                                       min = 1990, max = 2015,
                                       value = 1975,
                                       step = 1, sep = "")
                           ),
                  
                  # Healthcare Panel
                  tabPanel("Air Pollution",
                           fluidRow(
                             column(7, leafletOutput("map2")),
                             column(5, plotlyOutput("plot2"))
                           ),
                           wellPanel(
                             fluidRow(
                               column(12, sliderInput("slider_2",
                                                     label ="Year",
                                                     min = 2000, max = 2017,
                                                     value = 2008,
                                                     step = 1, sep = "")
                                      ),
                               column(12,
                                      span("In this tab we fake healthcare data over time.")))
                           )
                  )
        ), style = 'width: 800px')) # ; height: 1000px'))

# Define server logic
server <- function(input, output) {

  # Leaflet Map 1
  output$map1 <- renderLeaflet({
    leaflet() %>% setView(lng = -120, lat = 55, zoom = 4) %>% addTiles() 
  })
  
  # Leaflet Map 2
  filtered_data_map2 <- reactive({fire_df_heat %>%
      filter(FIRE_YEAR == as.numeric(input$slider_2))})
  
  output$map2 <- renderLeaflet({
    leaflet() %>% addProviderTiles(providers$Stamen.TonerLite,
                                   options = providerTileOptions(noWrap = TRUE))  %>%
      setView(-120, 55, 4) %>%
      addHeatmap(lng = ~LONGITUDE, lat = ~LATITUDE, intensity = ~intensity,
                 blur = 4, max = 0.01, radius = 2, data = filtered_data_map2())
  })
  
  # Plot 2
  filtered_data_ppm <- reactive({ co_data_year %>%
      filter(year <= as.numeric(input$slider_2))})
  
  output$plot2 <- renderPlotly({
    plot_ly(data = filtered_data_ppm(), x = ~year, y = ~avg, 
            type = "scatter", mode = "markers", color = ~I(color)) %>%
      layout(yaxis = list(title = "CO PPM per Year", range = c(0, 0.6)),
             xaxis = list(title = "", 
                          range = c(1999, 2019), tickangle = -45, dtick = 3, tick0 = 2000))
  })
}

# Run the application 
shinyApp(ui = ui, server = server)
