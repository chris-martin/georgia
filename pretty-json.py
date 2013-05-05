r"""Command-line tool to pretty-print JSON

Forked from

    https://github.com/simplejson/simplejson/blob/master/simplejson/tool.py

to modify the output format.

Usage::

    $ echo '{"json":"obj"}' | python pretty-json.py
    {
      "json": "obj"
    }

"""
from __future__ import with_statement
import sys
import simplejson as json

def main():
    infile = sys.stdin
    outfile = sys.stdout
    with infile:
        obj = json.load(infile, object_pairs_hook=json.OrderedDict, use_decimal=True)
    with outfile:
        json.dump(obj, outfile, sort_keys=False, indent='  ', use_decimal=True)
        outfile.write('\n')

if __name__ == '__main__':
    main()
