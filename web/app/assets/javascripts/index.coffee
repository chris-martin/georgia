$( ->

  log = (x) -> if window.console then console.log(x)

  $.ajax '/api/totals',
    type: 'GET'
    dataType: 'json'
    error: (jqXHR, textStatus, errorThrown) -> log(errorThrown)
    success: (data, textStatus, jqXHR) -> mkGraph(data)

  mkGraph = (data) ->

    graph = new Rickshaw.Graph(
      element: document.querySelector('.chart .body')
      renderer: 'bar'
      stroke: true
      min: 'auto'
      padding:
        bottom: 1.5
      series: [
        {
          name: 'Employee pay'
          data:
            data.totals.map((year) -> (
              x: +(new Date(year.year, 0, 0))/1000
              y: year['salary']
            ))
          color: 'rgba(50,120,205,0.5)'
          stroke: 'rgba(0,0,0,0.15)'
        }
      ]
    )

    graph.render()

    xAxis = new Rickshaw.Graph.Axis.Time(
      graph: graph
    )

    xAxis.render()

    yAxis = new Rickshaw.Graph.Axis.Y(
      graph: graph
      tickFormat: (x) => "$" + x/1000000000 + " billion"
      ticks: 2
      orientation: 'left'
      element: document.querySelector('.chart .yAxis')
    )

    yAxis.render()

    legend = new Rickshaw.Graph.Legend(
      graph: graph
      element: document.querySelector('.chart .legend')
    )

)
