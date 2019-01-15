import {join} from 'path'
import {launch} from 'puppeteer'

class Driver {
  private site: string
  constructor(site: string) {
    this.site = site
  }

  public setup() {
    return launch()
      .then(async (browser) => {
        const page = await browser.newPage()

        page.on('console', (msg) => {
          // tslint:disable-next-line:no-console
          console.log('PAGE LOG:', msg.text())
        })

        await page.goto(this.site)
        await page.addScriptTag({
          path: join(__dirname, 'node_modules', 'enyzme', 'build', 'index.js'),
          type: 'module',
        })
      })
  }
}

describe('basic-int', () => {
  const d = new Driver('http://bengreenier.com')
  return d.setup()
})
